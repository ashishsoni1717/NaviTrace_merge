
package org.navitrace.web;

import com.google.inject.Provider;
import org.navitrace.api.security.PermissionsService;
import org.navitrace.model.Server;
import org.navitrace.storage.StorageException;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class OverrideFilter implements Filter {

    private final Provider<PermissionsService> permissionsServiceProvider;

    @Inject
    public OverrideFilter(Provider<PermissionsService> permissionsServiceProvider) {
        this.permissionsServiceProvider = permissionsServiceProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (((HttpServletRequest) request).getServletPath().startsWith("/api")) {
            chain.doFilter(request, response);
            return;
        }

        ResponseWrapper wrappedResponse = new ResponseWrapper((HttpServletResponse) response);

        chain.doFilter(request, wrappedResponse);

        byte[] bytes = wrappedResponse.getCapture();
        if (bytes != null) {
            if (wrappedResponse.getContentType() != null && wrappedResponse.getContentType().contains("text/html")
                    || ((HttpServletRequest) request).getPathInfo().endsWith("manifest.webmanifest")) {

                Server server;
                try {
                    server = permissionsServiceProvider.get().getServer();
                } catch (StorageException e) {
                    throw new RuntimeException(e);
                }

                String title = server.getString("title", "Traccar");
                String description = server.getString("description", "Traccar GPS Tracking System");
                String colorPrimary = server.getString("colorPrimary", "#1a237e");

                String alteredContent = new String(wrappedResponse.getCapture())
                        .replace("${title}", title)
                        .replace("${description}", description)
                        .replace("${colorPrimary}", colorPrimary);

                byte[] data = alteredContent.getBytes();
                response.setContentLength(data.length);
                response.getOutputStream().write(data);

            } else {
                response.getOutputStream().write(bytes);
            }
        }
    }

}

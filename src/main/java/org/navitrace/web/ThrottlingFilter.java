
package org.navitrace.web;

import org.eclipse.jetty.servlets.DoSFilter;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Singleton
public class ThrottlingFilter extends DoSFilter {

    @Inject
    private Config config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        if (config.hasKey(Keys.WEB_MAX_REQUESTS_PER_SECOND)) {
            setMaxRequestsPerSec(config.getInteger(Keys.WEB_MAX_REQUESTS_PER_SECOND));
        }
        setMaxRequestMs(config.getInteger(Keys.WEB_MAX_REQUEST_SECONDS) * 1000L);
    }

    @Override
    protected String extractUserId(ServletRequest request) {
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session != null) {
            var userId = session.getAttribute("userId");
            return userId != null ? userId.toString() : null;
        }
        return null;
    }
}


package org.navitrace.web;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.util.resource.Resource;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import jakarta.inject.Inject;
import java.io.File;
import java.io.IOException;

public class DefaultOverrideServlet extends DefaultServlet {

    private Resource overrideResource;

    @Inject
    public DefaultOverrideServlet(Config config) {
        String override = config.getString(Keys.WEB_OVERRIDE);
        if (override != null) {
            overrideResource = Resource.newResource(new File(override));
        }
    }

    @Override
    public Resource getResource(String pathInContext) {
        if (overrideResource != null) {
            try {
                Resource override = overrideResource.addPath(pathInContext);
                if (override.exists()) {
                    return override;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return super.getResource(pathInContext.indexOf('.') < 0 ? "/" : pathInContext);
    }

    @Override
    public String getWelcomeFile(String pathInContext) {
        return super.getWelcomeFile("/");
    }

}

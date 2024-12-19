
package org.navitrace.web;

import com.google.inject.servlet.ServletModule;
import org.navitrace.api.AsyncSocketServlet;
import org.navitrace.api.MediaFilter;

public class WebModule extends ServletModule {

    @Override
    protected void configureServlets() {
        filter("/*").through(OverrideFilter.class);
        filter("/api/*").through(ThrottlingFilter.class);
        filter("/api/media/*").through(MediaFilter.class);
        serve("/api/socket").with(AsyncSocketServlet.class);
    }
}

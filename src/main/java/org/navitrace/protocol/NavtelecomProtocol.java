
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class NavtelecomProtocol extends BaseProtocol {

    @Inject
    public NavtelecomProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new NavtelecomFrameDecoder());
                pipeline.addLast(new NavtelecomProtocolDecoder(NavtelecomProtocol.this));
            }
        });
    }

}

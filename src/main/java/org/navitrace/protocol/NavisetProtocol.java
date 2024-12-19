
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class NavisetProtocol extends BaseProtocol {

    @Inject
    public NavisetProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new NavisetFrameDecoder());
                pipeline.addLast(new NavisetProtocolDecoder(NavisetProtocol.this));
            }
        });
    }

}

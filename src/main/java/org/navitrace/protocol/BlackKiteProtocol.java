
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class BlackKiteProtocol extends BaseProtocol {

    @Inject
    public BlackKiteProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new GalileoFrameDecoder());
                pipeline.addLast(new BlackKiteProtocolDecoder(BlackKiteProtocol.this));
            }
        });
    }

}


package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class OmnicommProtocol extends BaseProtocol {

    @Inject
    public OmnicommProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new OmnicommFrameDecoder());
                pipeline.addLast(new OmnicommProtocolDecoder(OmnicommProtocol.this));
            }
        });
    }

}

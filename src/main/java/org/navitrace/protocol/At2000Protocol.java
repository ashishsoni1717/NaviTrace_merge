
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class At2000Protocol extends BaseProtocol {

    @Inject
    public At2000Protocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new At2000FrameDecoder());
                pipeline.addLast(new At2000ProtocolDecoder(At2000Protocol.this));
            }
        });
    }

}

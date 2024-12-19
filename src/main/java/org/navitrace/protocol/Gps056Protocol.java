
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class Gps056Protocol extends BaseProtocol {

    @Inject
    public Gps056Protocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new Gps056FrameDecoder());
                pipeline.addLast(new Gps056ProtocolDecoder(Gps056Protocol.this));
            }
        });
    }

}

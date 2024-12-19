
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class Vt200Protocol extends BaseProtocol {

    @Inject
    public Vt200Protocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new Vt200FrameDecoder());
                pipeline.addLast(new Vt200ProtocolDecoder(Vt200Protocol.this));
            }
        });
    }

}

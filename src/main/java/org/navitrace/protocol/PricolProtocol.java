
package org.navitrace.protocol;

import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class PricolProtocol extends BaseProtocol {

    @Inject
    public PricolProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new FixedLengthFrameDecoder(64));
                pipeline.addLast(new PricolProtocolDecoder(PricolProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new PricolProtocolDecoder(PricolProtocol.this));
            }
        });
    }

}

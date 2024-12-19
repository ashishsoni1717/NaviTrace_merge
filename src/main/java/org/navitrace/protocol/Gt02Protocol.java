
package org.navitrace.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class Gt02Protocol extends BaseProtocol {

    @Inject
    public Gt02Protocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(256, 2, 1, 2, 0));
                pipeline.addLast(new Gt02ProtocolDecoder(Gt02Protocol.this));
            }
        });
    }

}
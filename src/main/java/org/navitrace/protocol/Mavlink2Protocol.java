
package org.navitrace.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class Mavlink2Protocol extends BaseProtocol {

    @Inject
    public Mavlink2Protocol(Config config) {
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 1, 1, 10, 0));
                pipeline.addLast(new Mavlink2ProtocolDecoder(Mavlink2Protocol.this));
            }
        });
    }

}

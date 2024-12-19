
package org.navitrace.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import java.nio.ByteOrder;
import jakarta.inject.Inject;

public class RecodaProtocol extends BaseProtocol {

    @Inject
    public RecodaProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 1024, 4, 4, -8, 0, true));
                pipeline.addLast(new RecodaProtocolDecoder(RecodaProtocol.this));
            }
        });
    }

}

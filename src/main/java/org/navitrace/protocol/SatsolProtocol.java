
package org.navitrace.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import java.nio.ByteOrder;

import jakarta.inject.Inject;

public class SatsolProtocol extends BaseProtocol {

    @Inject
    public SatsolProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 1400, 8, 2, 0, 0, true));
                pipeline.addLast(new SatsolProtocolDecoder(SatsolProtocol.this));
            }
        });
    }

}

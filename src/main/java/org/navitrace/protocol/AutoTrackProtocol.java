
package org.navitrace.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import java.nio.ByteOrder;
import jakarta.inject.Inject;

public class AutoTrackProtocol extends BaseProtocol {

    @Inject
    public AutoTrackProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 1024, 5, 2, 2, 0, true));
                pipeline.addLast(new AutoTrackProtocolDecoder(AutoTrackProtocol.this));
            }
        });
    }

}

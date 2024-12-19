
package org.navitrace.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class ObdDongleProtocol extends BaseProtocol {

    @Inject
    public ObdDongleProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(1099, 20, 2, 3, 0));
                pipeline.addLast(new ObdDongleProtocolDecoder(ObdDongleProtocol.this));
            }
        });
    }

}

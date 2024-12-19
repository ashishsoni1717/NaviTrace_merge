
package org.navitrace.protocol;

import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class M2mProtocol extends BaseProtocol {

    @Inject
    public M2mProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new FixedLengthFrameDecoder(23));
                pipeline.addLast(new M2mProtocolDecoder(M2mProtocol.this));
            }
        });
    }

}

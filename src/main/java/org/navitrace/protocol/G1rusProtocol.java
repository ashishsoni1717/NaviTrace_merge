
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class G1rusProtocol extends BaseProtocol {

    @Inject
    public G1rusProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new G1rusFrameDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new G1rusProtocolDecoder(G1rusProtocol.this));
            }
        });
    }

}

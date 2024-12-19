
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class AisProtocol extends BaseProtocol {

    @Inject
    public AisProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new AisProtocolDecoder(AisProtocol.this));
            }
        });
    }

}

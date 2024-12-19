
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class AplicomProtocol extends BaseProtocol {

    @Inject
    public AplicomProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new AplicomFrameDecoder());
                pipeline.addLast(new AplicomProtocolDecoder(AplicomProtocol.this));
            }
        });
    }

}

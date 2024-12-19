
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class TramigoProtocol extends BaseProtocol {

    @Inject
    public TramigoProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new TramigoFrameDecoder());
                pipeline.addLast(new TramigoProtocolDecoder(TramigoProtocol.this));
            }
        });
    }

}

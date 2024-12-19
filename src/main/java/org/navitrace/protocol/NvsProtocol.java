
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class NvsProtocol extends BaseProtocol {

    @Inject
    public NvsProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new NvsFrameDecoder());
                pipeline.addLast(new NvsProtocolDecoder(NvsProtocol.this));
            }
        });
    }

}

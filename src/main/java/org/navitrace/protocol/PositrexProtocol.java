
package org.navitrace.protocol;

import jakarta.inject.Inject;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

public class PositrexProtocol extends BaseProtocol {

    @Inject
    public PositrexProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new PositrexProtocolDecoder(PositrexProtocol.this));
            }
        });
    }

}

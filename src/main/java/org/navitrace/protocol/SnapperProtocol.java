
package org.navitrace.protocol;

import jakarta.inject.Inject;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

public class SnapperProtocol extends BaseProtocol {

    @Inject
    public SnapperProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new SnapperFrameDecoder());
                pipeline.addLast(new SnapperProtocolDecoder(SnapperProtocol.this));
            }
        });
    }

}

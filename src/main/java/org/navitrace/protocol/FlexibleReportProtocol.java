
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class FlexibleReportProtocol extends BaseProtocol {

    @Inject
    public FlexibleReportProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new FlexibleReportProtocolDecoder(FlexibleReportProtocol.this));
            }
        });
    }

}


package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class GalileoProtocol extends BaseProtocol {

    @Inject
    public GalileoProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_CUSTOM,
                Command.TYPE_OUTPUT_CONTROL);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new GalileoFrameDecoder());
                pipeline.addLast(new GalileoProtocolEncoder(GalileoProtocol.this));
                pipeline.addLast(new GalileoProtocolDecoder(GalileoProtocol.this));
            }
        });
    }

}

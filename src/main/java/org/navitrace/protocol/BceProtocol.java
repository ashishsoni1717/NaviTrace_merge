
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class BceProtocol extends BaseProtocol {

    @Inject
    public BceProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_OUTPUT_CONTROL);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new BceFrameDecoder());
                pipeline.addLast(new BceProtocolEncoder(BceProtocol.this));
                pipeline.addLast(new BceProtocolDecoder(BceProtocol.this));
            }
        });
    }

}

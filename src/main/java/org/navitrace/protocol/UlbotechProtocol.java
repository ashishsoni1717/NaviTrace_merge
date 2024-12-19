
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class UlbotechProtocol extends BaseProtocol {

    @Inject
    public UlbotechProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_CUSTOM);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new UlbotechFrameDecoder());
                pipeline.addLast(new UlbotechProtocolEncoder(UlbotechProtocol.this));
                pipeline.addLast(new UlbotechProtocolDecoder(UlbotechProtocol.this));
            }
        });
    }

}

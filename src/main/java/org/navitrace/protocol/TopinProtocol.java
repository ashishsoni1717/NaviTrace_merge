
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class TopinProtocol extends BaseProtocol {

    @Inject
    public TopinProtocol(Config config) {
        if (!config.getBoolean(Keys.PROTOCOL_DISABLE_COMMANDS.withPrefix(getName()))) {
            setSupportedDataCommands(
                    Command.TYPE_SOS_NUMBER);
        }
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new TopinProtocolEncoder(TopinProtocol.this));
                pipeline.addLast(new TopinProtocolDecoder(TopinProtocol.this));
            }
        });
    }

}

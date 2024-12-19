
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class PstProtocol extends BaseProtocol {

    @Inject
    public PstProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME);
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new PstProtocolEncoder(PstProtocol.this));
                pipeline.addLast(new PstProtocolDecoder(PstProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new PstFrameEncoder());
                pipeline.addLast(new PstFrameDecoder());
                pipeline.addLast(new PstProtocolEncoder(PstProtocol.this));
                pipeline.addLast(new PstProtocolDecoder(PstProtocol.this));
            }
        });
    }

}

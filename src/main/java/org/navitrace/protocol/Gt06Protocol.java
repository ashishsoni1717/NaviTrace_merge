
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class Gt06Protocol extends BaseProtocol {

    @Inject
    public Gt06Protocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME,
                Command.TYPE_CUSTOM);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new Gt06FrameDecoder());
                pipeline.addLast(new Gt06ProtocolEncoder(Gt06Protocol.this));
                pipeline.addLast(new Gt06ProtocolDecoder(Gt06Protocol.this));
            }
        });
    }

}

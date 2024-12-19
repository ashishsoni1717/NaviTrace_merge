
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class Gl200Protocol extends BaseProtocol {

    @Inject
    public Gl200Protocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_POSITION_SINGLE,
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME,
                Command.TYPE_IDENTIFICATION,
                Command.TYPE_REBOOT_DEVICE);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new Gl200FrameDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new Gl200ProtocolEncoder(Gl200Protocol.this));
                pipeline.addLast(new Gl200ProtocolDecoder(Gl200Protocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new Gl200ProtocolEncoder(Gl200Protocol.this));
                pipeline.addLast(new Gl200ProtocolDecoder(Gl200Protocol.this));
            }
        });
    }

}

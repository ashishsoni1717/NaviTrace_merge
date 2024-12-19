
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class Jt600Protocol extends BaseProtocol {

    @Inject
    public Jt600Protocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_ENGINE_RESUME,
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_SET_TIMEZONE,
                Command.TYPE_REBOOT_DEVICE);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new Jt600FrameDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new Jt600ProtocolEncoder(Jt600Protocol.this));
                pipeline.addLast(new Jt600ProtocolDecoder(Jt600Protocol.this));
            }
        });
    }

}


package org.navitrace.protocol;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class TotemProtocol extends BaseProtocol {

    @Inject
    public TotemProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_CUSTOM,
                Command.TYPE_REBOOT_DEVICE,
                Command.TYPE_FACTORY_RESET,
                Command.TYPE_GET_VERSION,
                Command.TYPE_POSITION_SINGLE,
                Command.TYPE_ENGINE_RESUME,
                Command.TYPE_ENGINE_STOP
        );

        setTextCommandEncoder(new TotemProtocolSmsEncoder(this));
        setSupportedTextCommands(
                Command.TYPE_CUSTOM,
                Command.TYPE_REBOOT_DEVICE,
                Command.TYPE_FACTORY_RESET,
                Command.TYPE_GET_VERSION,
                Command.TYPE_POSITION_SINGLE,
                Command.TYPE_ENGINE_RESUME,
                Command.TYPE_ENGINE_STOP
        );

        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new TotemFrameDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new TotemProtocolEncoder(TotemProtocol.this));
                pipeline.addLast(new TotemProtocolDecoder(TotemProtocol.this));
            }
        });
    }

}

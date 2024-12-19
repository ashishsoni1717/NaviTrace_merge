
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class H02Protocol extends BaseProtocol {

    @Inject
    public H02Protocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_ALARM_ARM,
                Command.TYPE_ALARM_DISARM,
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME,
                Command.TYPE_POSITION_PERIODIC
        );
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                int messageLength = config.getInteger(Keys.PROTOCOL_MESSAGE_LENGTH.withPrefix(getName()));
                pipeline.addLast(new H02FrameDecoder(messageLength));
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new H02ProtocolEncoder(H02Protocol.this));
                pipeline.addLast(new H02ProtocolDecoder(H02Protocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new H02ProtocolEncoder(H02Protocol.this));
                pipeline.addLast(new H02ProtocolDecoder(H02Protocol.this));
            }
        });
    }
}

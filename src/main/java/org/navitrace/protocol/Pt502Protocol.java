
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class Pt502Protocol extends BaseProtocol {

    @Inject
    public Pt502Protocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_CUSTOM,
                Command.TYPE_SET_TIMEZONE,
                Command.TYPE_ALARM_SPEED,
                Command.TYPE_OUTPUT_CONTROL,
                Command.TYPE_REQUEST_PHOTO);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new Pt502FrameDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new Pt502ProtocolEncoder(Pt502Protocol.this));
                pipeline.addLast(new Pt502ProtocolDecoder(Pt502Protocol.this));
            }
        });
    }

}


package org.navitrace.protocol;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.CharacterDelimiterFrameDecoder;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class MiniFinderProtocol extends BaseProtocol {

    @Inject
    public MiniFinderProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_SET_TIMEZONE,
                Command.TYPE_VOICE_MONITORING,
                Command.TYPE_ALARM_SPEED,
                Command.TYPE_ALARM_GEOFENCE,
                Command.TYPE_ALARM_VIBRATION,
                Command.TYPE_SET_AGPS,
                Command.TYPE_ALARM_FALL,
                Command.TYPE_MODE_POWER_SAVING,
                Command.TYPE_MODE_DEEP_SLEEP,
                Command.TYPE_SOS_NUMBER,
                Command.TYPE_SET_INDICATOR);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new CharacterDelimiterFrameDecoder(1024, ";\0", ";"));
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new MiniFinderProtocolEncoder(MiniFinderProtocol.this));
                pipeline.addLast(new MiniFinderProtocolDecoder(MiniFinderProtocol.this));
            }
        });
    }

}

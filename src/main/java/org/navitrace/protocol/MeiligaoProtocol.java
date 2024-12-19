
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class MeiligaoProtocol extends BaseProtocol {

    @Inject
    public MeiligaoProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_POSITION_SINGLE,
                Command.TYPE_POSITION_PERIODIC,
                Command.TYPE_OUTPUT_CONTROL,
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME,
                Command.TYPE_ALARM_GEOFENCE,
                Command.TYPE_SET_TIMEZONE,
                Command.TYPE_REQUEST_PHOTO,
                Command.TYPE_REBOOT_DEVICE);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new MeiligaoFrameDecoder());
                pipeline.addLast(new MeiligaoProtocolEncoder(MeiligaoProtocol.this));
                pipeline.addLast(new MeiligaoProtocolDecoder(MeiligaoProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new MeiligaoProtocolEncoder(MeiligaoProtocol.this));
                pipeline.addLast(new MeiligaoProtocolDecoder(MeiligaoProtocol.this));
            }
        });
    }

}

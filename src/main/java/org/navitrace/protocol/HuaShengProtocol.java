
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class HuaShengProtocol extends BaseProtocol {

    @Inject
    public HuaShengProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_POSITION_PERIODIC,
                Command.TYPE_OUTPUT_CONTROL,
                Command.TYPE_ALARM_ARM,
                Command.TYPE_ALARM_DISARM,
                Command.TYPE_SET_SPEED_LIMIT);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new HuaShengFrameDecoder());
                pipeline.addLast(new HuaShengProtocolEncoder(HuaShengProtocol.this));
                pipeline.addLast(new HuaShengProtocolDecoder(HuaShengProtocol.this));
            }
        });
    }

}

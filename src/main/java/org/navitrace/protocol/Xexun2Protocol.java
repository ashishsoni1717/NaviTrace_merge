
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class Xexun2Protocol extends BaseProtocol {

    @Inject
    public Xexun2Protocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_CUSTOM,
                Command.TYPE_POSITION_PERIODIC,
                Command.TYPE_POWER_OFF,
                Command.TYPE_REBOOT_DEVICE);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new Xexun2FrameEncoder());
                pipeline.addLast(new Xexun2FrameDecoder());
                pipeline.addLast(new Xexun2ProtocolDecoder(Xexun2Protocol.this));
                pipeline.addLast(new Xexun2ProtocolEncoder(Xexun2Protocol.this));
            }
        });
    }

}

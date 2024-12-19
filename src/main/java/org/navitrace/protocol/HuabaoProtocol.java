
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class HuabaoProtocol extends BaseProtocol {

    @Inject
    public HuabaoProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new HuabaoFrameEncoder());
                pipeline.addLast(new HuabaoFrameDecoder());
                pipeline.addLast(new HuabaoProtocolEncoder(HuabaoProtocol.this));
                pipeline.addLast(new HuabaoProtocolDecoder(HuabaoProtocol.this));
            }
        });
    }

}


package org.navitrace.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class KhdProtocol extends BaseProtocol {

    @Inject
    public KhdProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME,
                Command.TYPE_GET_VERSION,
                Command.TYPE_FACTORY_RESET,
                Command.TYPE_SET_SPEED_LIMIT,
                Command.TYPE_SET_ODOMETER,
                Command.TYPE_POSITION_SINGLE);

        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(512, 3, 2));
                pipeline.addLast(new KhdProtocolEncoder(KhdProtocol.this));
                pipeline.addLast(new KhdProtocolDecoder(KhdProtocol.this));
            }
        });
    }

}

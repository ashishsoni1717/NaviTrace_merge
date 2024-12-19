
package org.navitrace.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class EnforaProtocol extends BaseProtocol {

    @Inject
    public EnforaProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_CUSTOM,
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, -2, 2));
                pipeline.addLast(new EnforaProtocolEncoder(EnforaProtocol.this));
                pipeline.addLast(new EnforaProtocolDecoder(EnforaProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new EnforaProtocolEncoder(EnforaProtocol.this));
                pipeline.addLast(new EnforaProtocolDecoder(EnforaProtocol.this));
            }
        });
    }

}

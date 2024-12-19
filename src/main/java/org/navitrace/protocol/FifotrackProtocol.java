
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class FifotrackProtocol extends BaseProtocol {

    @Inject
    public FifotrackProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_CUSTOM,
                Command.TYPE_REQUEST_PHOTO);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new FifotrackFrameDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new FifotrackProtocolEncoder(FifotrackProtocol.this));
                pipeline.addLast(new FifotrackProtocolDecoder(FifotrackProtocol.this));
            }
        });
    }

}

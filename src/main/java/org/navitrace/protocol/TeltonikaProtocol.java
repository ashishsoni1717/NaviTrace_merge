
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class TeltonikaProtocol extends BaseProtocol {

    @Inject
    public TeltonikaProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_CUSTOM);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new TeltonikaFrameDecoder());
                pipeline.addLast(new TeltonikaProtocolEncoder(TeltonikaProtocol.this));
                pipeline.addLast(new TeltonikaProtocolDecoder(TeltonikaProtocol.this, false));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new TeltonikaProtocolEncoder(TeltonikaProtocol.this));
                pipeline.addLast(new TeltonikaProtocolDecoder(TeltonikaProtocol.this, true));
            }
        });
    }

}

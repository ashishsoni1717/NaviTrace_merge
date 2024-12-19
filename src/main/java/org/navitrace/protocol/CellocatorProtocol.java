
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class CellocatorProtocol extends BaseProtocol {

    @Inject
    public CellocatorProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_OUTPUT_CONTROL);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new CellocatorFrameDecoder());
                pipeline.addLast(new CellocatorProtocolEncoder(CellocatorProtocol.this));
                pipeline.addLast(new CellocatorProtocolDecoder(CellocatorProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new CellocatorProtocolEncoder(CellocatorProtocol.this));
                pipeline.addLast(new CellocatorProtocolDecoder(CellocatorProtocol.this));
            }
        });
    }

}

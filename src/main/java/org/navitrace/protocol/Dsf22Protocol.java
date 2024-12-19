
package org.navitrace.protocol;

import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class Dsf22Protocol extends BaseProtocol {

    @Inject
    public Dsf22Protocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new Dsf22FrameDecoder());
                pipeline.addLast(new Dsf22ProtocolDecoder(Dsf22Protocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new Dsf22ProtocolDecoder(Dsf22Protocol.this));
            }
        });
    }

}

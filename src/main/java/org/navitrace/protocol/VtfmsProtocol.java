
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class VtfmsProtocol extends BaseProtocol {

    @Inject
    public VtfmsProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new VtfmsFrameDecoder());
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new VtfmsProtocolDecoder(VtfmsProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new VtfmsProtocolDecoder(VtfmsProtocol.this));
            }
        });
    }

}

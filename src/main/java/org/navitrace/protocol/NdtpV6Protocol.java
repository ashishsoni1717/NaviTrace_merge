
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class NdtpV6Protocol extends BaseProtocol {

    @Inject
    public NdtpV6Protocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new NdtpV6ProtocolDecoder(NdtpV6Protocol.this));
            }
        });
    }

}

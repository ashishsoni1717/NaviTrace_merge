
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class JpKorjarProtocol extends BaseProtocol {

    @Inject
    public JpKorjarProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new JpKorjarFrameDecoder());
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new JpKorjarProtocolDecoder(JpKorjarProtocol.this));
            }
        });
    }

}

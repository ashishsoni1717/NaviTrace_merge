
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.CharacterDelimiterFrameDecoder;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class CarTrackProtocol extends BaseProtocol {

    @Inject
    public CarTrackProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new CharacterDelimiterFrameDecoder(1024, "##"));
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new CarTrackProtocolDecoder(CarTrackProtocol.this));
            }
        });
    }

}


package org.navitrace.protocol;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.CharacterDelimiterFrameDecoder;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

import jakarta.inject.Inject;
import org.navitrace.config.Keys;

public class TaipProtocol extends BaseProtocol {

    @Inject
    public TaipProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new CharacterDelimiterFrameDecoder(1024, '<'));
                if (config.getBoolean(Keys.PROTOCOL_PREFIX.withPrefix(getName()))) {
                    pipeline.addLast(new TaipPrefixEncoder());
                }
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new TaipProtocolDecoder(TaipProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                if (config.getBoolean(Keys.PROTOCOL_PREFIX.withPrefix(getName()))) {
                    pipeline.addLast(new TaipPrefixEncoder());
                }
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new TaipProtocolDecoder(TaipProtocol.this));
            }
        });
    }

}

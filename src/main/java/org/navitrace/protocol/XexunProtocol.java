
package org.navitrace.protocol;

import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class XexunProtocol extends BaseProtocol {

    @Inject
    public XexunProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                boolean full = config.getBoolean(Keys.PROTOCOL_EXTENDED.withPrefix(getName()));
                if (full) {
                    pipeline.addLast(new LineBasedFrameDecoder(1024)); // tracker bug \n\r
                } else {
                    pipeline.addLast(new XexunFrameDecoder());
                }
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new XexunProtocolEncoder(XexunProtocol.this));
                pipeline.addLast(new XexunProtocolDecoder(XexunProtocol.this, full));
            }
        });
    }

}

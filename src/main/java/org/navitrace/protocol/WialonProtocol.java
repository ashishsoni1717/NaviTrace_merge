
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

import java.nio.charset.StandardCharsets;

import jakarta.inject.Inject;

public class WialonProtocol extends BaseProtocol {

    @Inject
    public WialonProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_REBOOT_DEVICE,
                Command.TYPE_SEND_USSD,
                Command.TYPE_IDENTIFICATION,
                Command.TYPE_OUTPUT_CONTROL);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LineBasedFrameDecoder(4 * 1024));
                if (config.getBoolean(Keys.PROTOCOL_UTF8.withPrefix(getName()))) {
                    pipeline.addLast(new StringEncoder(StandardCharsets.UTF_8));
                    pipeline.addLast(new StringDecoder(StandardCharsets.UTF_8));
                } else {
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());
                }
                pipeline.addLast(new WialonProtocolEncoder(WialonProtocol.this));
                pipeline.addLast(new WialonProtocolDecoder(WialonProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                if (config.getBoolean(Keys.PROTOCOL_UTF8.withPrefix(getName()))) {
                    pipeline.addLast(new StringEncoder(StandardCharsets.UTF_8));
                    pipeline.addLast(new StringDecoder(StandardCharsets.UTF_8));
                } else {
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());
                }
                pipeline.addLast(new WialonProtocolEncoder(WialonProtocol.this));
                pipeline.addLast(new WialonProtocolDecoder(WialonProtocol.this));
            }
        });
    }

}

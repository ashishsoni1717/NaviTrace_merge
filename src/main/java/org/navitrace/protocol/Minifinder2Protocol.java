
package org.navitrace.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import java.nio.ByteOrder;

import jakarta.inject.Inject;

public class Minifinder2Protocol extends BaseProtocol {

    @Inject
    public Minifinder2Protocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_FIRMWARE_UPDATE,
                Command.TYPE_CONFIGURATION);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 2048, 2, 2, 4, 0, true));
                pipeline.addLast(new Minifinder2ProtocolEncoder(Minifinder2Protocol.this));
                pipeline.addLast(new Minifinder2ProtocolDecoder(Minifinder2Protocol.this));
            }
        });
    }

}

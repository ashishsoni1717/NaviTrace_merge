
package org.navitrace.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class T800xProtocol extends BaseProtocol {

    @Inject
    public T800xProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_CUSTOM);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 3, 2, -5, 0));
                pipeline.addLast(new T800xProtocolEncoder(T800xProtocol.this));
                pipeline.addLast(new T800xProtocolDecoder(T800xProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new T800xProtocolEncoder(T800xProtocol.this));
                pipeline.addLast(new T800xProtocolDecoder(T800xProtocol.this));
            }
        });
    }

}

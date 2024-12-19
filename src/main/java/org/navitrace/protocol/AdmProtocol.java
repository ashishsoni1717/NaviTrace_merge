
package org.navitrace.protocol;

import io.netty.handler.codec.string.StringEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.model.Command;

import jakarta.inject.Inject;

public class AdmProtocol extends BaseProtocol {

    @Inject
    public AdmProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_GET_DEVICE_STATUS,
                Command.TYPE_CUSTOM);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new AdmFrameDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new AdmProtocolEncoder(AdmProtocol.this));
                pipeline.addLast(new AdmProtocolDecoder(AdmProtocol.this));
            }
        });
    }

}

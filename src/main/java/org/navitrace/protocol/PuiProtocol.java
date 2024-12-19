
package org.navitrace.protocol;

import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import jakarta.inject.Inject;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

public class PuiProtocol extends BaseProtocol {

    @Inject
    public PuiProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(MqttEncoder.INSTANCE);
                pipeline.addLast(new MqttDecoder());
                pipeline.addLast(new PuiProtocolDecoder(PuiProtocol.this));
            }
        });
    }

}


package org.navitrace.protocol;

import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import jakarta.inject.Inject;

public class Mta6Protocol extends BaseProtocol {

    @Inject
    public Mta6Protocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new HttpResponseEncoder());
                pipeline.addLast(new HttpRequestDecoder());
                pipeline.addLast(new HttpObjectAggregator(65535));
                pipeline.addLast(new Mta6ProtocolDecoder(
                        Mta6Protocol.this, !config.getBoolean(Keys.PROTOCOL_CAN.withPrefix(getName()))));
            }
        });
    }

}

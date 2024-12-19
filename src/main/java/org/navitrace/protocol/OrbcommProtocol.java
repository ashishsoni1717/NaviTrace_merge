
package org.navitrace.protocol;

import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerClient;
import org.navitrace.config.Config;

import jakarta.inject.Inject;

public class OrbcommProtocol extends BaseProtocol {

    @Inject
    public OrbcommProtocol(Config config) {
        addClient(new TrackerClient(config, getName()) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new HttpRequestEncoder());
                pipeline.addLast(new HttpResponseDecoder());
                pipeline.addLast(new HttpObjectAggregator(65535));
                pipeline.addLast(new OrbcommProtocolDecoder(OrbcommProtocol.this));
                pipeline.addLast(new OrbcommProtocolPoller(OrbcommProtocol.this, config));
            }
        });
    }

}

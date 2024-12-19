
package org.navitrace.protocol;

import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import jakarta.inject.Inject;
import org.navitrace.BaseProtocol;
import org.navitrace.PipelineBuilder;
import org.navitrace.TrackerServer;
import org.navitrace.config.Config;

public class ValtrackProtocol extends BaseProtocol {

    @Inject
    public ValtrackProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new HttpResponseEncoder());
                pipeline.addLast(new HttpRequestDecoder());
                pipeline.addLast(new HttpObjectAggregator(16384));
                pipeline.addLast(new ValtrackProtocolDecoder(ValtrackProtocol.this));
            }
        });
    }
}

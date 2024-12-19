
package org.navitrace;

import com.google.inject.Injector;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.handler.network.AcknowledgementHandler;
import org.navitrace.handler.network.MainEventHandler;
import org.navitrace.handler.network.NetworkForwarderHandler;
import org.navitrace.handler.network.NetworkMessageHandler;
import org.navitrace.handler.network.OpenChannelHandler;
import org.navitrace.handler.network.RemoteAddressHandler;
import org.navitrace.handler.network.StandardLoggingHandler;

import java.util.Map;

public abstract class BasePipelineFactory extends ChannelInitializer<Channel> {

    private final Injector injector;
    private final TrackerConnector connector;
    private final Config config;
    private final String protocol;
    private final int timeout;

    public BasePipelineFactory(TrackerConnector connector, Config config, String protocol) {
        this.injector = Main.getInjector();
        this.connector = connector;
        this.config = config;
        this.protocol = protocol;
        int timeout = config.getInteger(Keys.PROTOCOL_TIMEOUT.withPrefix(protocol));
        if (timeout == 0) {
            this.timeout = config.getInteger(Keys.SERVER_TIMEOUT);
        } else {
            this.timeout = timeout;
        }
    }

    protected abstract void addTransportHandlers(PipelineBuilder pipeline);

    protected abstract void addProtocolHandlers(PipelineBuilder pipeline);

    @SuppressWarnings("unchecked")
    public static <T extends ChannelHandler> T getHandler(ChannelPipeline pipeline, Class<T> clazz) {
        for (Map.Entry<String, ChannelHandler> handlerEntry : pipeline) {
            ChannelHandler handler = handlerEntry.getValue();
            if (handler instanceof WrapperInboundHandler wrapperHandler) {
                handler = wrapperHandler.getWrappedHandler();
            } else if (handler instanceof WrapperOutboundHandler wrapperHandler) {
                handler = wrapperHandler.getWrappedHandler();
            }
            if (clazz.isAssignableFrom(handler.getClass())) {
                return (T) handler;
            }
        }
        return null;
    }

    private <T> T injectMembers(T object) {
        injector.injectMembers(object);
        return object;
    }

    @Override
    protected void initChannel(Channel channel) {
        final ChannelPipeline pipeline = channel.pipeline();

        addTransportHandlers(pipeline::addLast);

        if (timeout > 0 && !connector.isDatagram()) {
            pipeline.addLast(new IdleStateHandler(timeout, 0, 0));
        }
        pipeline.addLast(new OpenChannelHandler(connector));
        if (config.hasKey(Keys.SERVER_FORWARD)) {
            int port = config.getInteger(Keys.PROTOCOL_PORT.withPrefix(protocol));
            pipeline.addLast(injectMembers(new NetworkForwarderHandler(port)));
        }
        pipeline.addLast(new NetworkMessageHandler());
        pipeline.addLast(injectMembers(new StandardLoggingHandler(protocol)));

        if (!connector.isDatagram() && !config.getBoolean(Keys.SERVER_INSTANT_ACKNOWLEDGEMENT)) {
            pipeline.addLast(new AcknowledgementHandler());
        }

        addProtocolHandlers(handler -> {
            if (handler instanceof BaseProtocolDecoder || handler instanceof BaseProtocolEncoder) {
                injectMembers(handler);
            } else {
                if (handler instanceof ChannelInboundHandler channelHandler) {
                    handler = new WrapperInboundHandler(channelHandler);
                } else if (handler instanceof ChannelOutboundHandler channelHandler) {
                    handler = new WrapperOutboundHandler(channelHandler);
                }
            }
            pipeline.addLast(handler);
        });

        pipeline.addLast(injector.getInstance(RemoteAddressHandler.class));
        pipeline.addLast(injector.getInstance(ProcessingHandler.class));
        pipeline.addLast(injector.getInstance(MainEventHandler.class));
    }

}

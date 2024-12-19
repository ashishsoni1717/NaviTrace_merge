
package org.navitrace.handler.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.timeout.IdleStateEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.BasePipelineFactory;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.helper.NetworkUtil;
import org.navitrace.session.ConnectionManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Singleton
@ChannelHandler.Sharable
public class MainEventHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainEventHandler.class);

    private final ConnectionManager connectionManager;
    private final Set<String> connectionlessProtocols = new HashSet<>();

    @Inject
    public MainEventHandler(Config config, ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        String connectionlessProtocolList = config.getString(Keys.STATUS_IGNORE_OFFLINE);
        if (connectionlessProtocolList != null) {
            connectionlessProtocols.addAll(Arrays.asList(connectionlessProtocolList.split("[, ]")));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (!(ctx.channel() instanceof DatagramChannel)) {
            LOGGER.info("[{}] connected", NetworkUtil.session(ctx.channel()));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOGGER.info("[{}] disconnected", NetworkUtil.session(ctx.channel()));
        closeChannel(ctx.channel());

        boolean supportsOffline = BasePipelineFactory.getHandler(ctx.pipeline(), HttpRequestDecoder.class) == null
                && !connectionlessProtocols.contains(ctx.pipeline().get(BaseProtocolDecoder.class).getProtocolName());
        connectionManager.deviceDisconnected(ctx.channel(), supportsOffline);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        LOGGER.info("[{}] error", NetworkUtil.session(ctx.channel()), cause);
        closeChannel(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            LOGGER.info("[{}] timed out", NetworkUtil.session(ctx.channel()));
            closeChannel(ctx.channel());
        }
    }

    private void closeChannel(Channel channel) {
        if (!(channel instanceof DatagramChannel)) {
            channel.close();
        }
    }

}

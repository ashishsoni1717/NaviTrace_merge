
package org.navitrace;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public abstract class BaseProtocolPoller extends ChannelDuplexHandler {

    private final long interval;
    private Future<?> timeout;

    public BaseProtocolPoller(long interval) {
        this.interval = interval;
    }

    protected abstract void sendRequest(Channel channel, SocketAddress remoteAddress);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (interval > 0) {
            timeout = ctx.executor().scheduleAtFixedRate(
                    () -> sendRequest(ctx.channel(), ctx.channel().remoteAddress()), 0, interval, TimeUnit.SECONDS);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (timeout != null) {
            timeout.cancel(false);
            timeout = null;
        }
    }

}

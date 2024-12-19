
package org.navitrace.handler.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Position;

import java.net.InetSocketAddress;

@Singleton
@ChannelHandler.Sharable
public class RemoteAddressHandler extends ChannelInboundHandlerAdapter {

    private final boolean enabled;

    @Inject
    public RemoteAddressHandler(Config config) {
        enabled = config.getBoolean(Keys.PROCESSING_REMOTE_ADDRESS_ENABLE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (enabled) {
            InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String hostAddress = remoteAddress != null ? remoteAddress.getAddress().getHostAddress() : null;

            if (msg instanceof Position position) {
                position.set(Position.KEY_IP, hostAddress);
            }
        }

        ctx.fireChannelRead(msg);
    }

}

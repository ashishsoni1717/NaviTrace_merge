
package org.navitrace;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.handler.network.AcknowledgementHandler;
import org.navitrace.helper.DataConverter;
import org.navitrace.model.Position;

import jakarta.inject.Inject;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public abstract class ExtendedObjectDecoder extends ChannelInboundHandlerAdapter {

    private Config config;

    public Config getConfig() {
        return config;
    }

    @Inject
    public void setConfig(Config config) {
        this.config = config;
        init();
    }

    /**
     * Method called when config is initialized.
     */
    protected void init() {
    }

    private void saveOriginal(Object decodedMessage, Object originalMessage) {
        if (getConfig().getBoolean(Keys.DATABASE_SAVE_ORIGINAL) && decodedMessage instanceof Position position) {
            if (originalMessage instanceof ByteBuf buf) {
                position.set(Position.KEY_ORIGINAL, ByteBufUtil.hexDump(buf, 0, buf.writerIndex()));
            } else if (originalMessage instanceof String stringMessage) {
                position.set(Position.KEY_ORIGINAL, DataConverter.printHex(
                        stringMessage.getBytes(StandardCharsets.US_ASCII)));
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NetworkMessage networkMessage = (NetworkMessage) msg;
        Object originalMessage = networkMessage.getMessage();
        ctx.writeAndFlush(new AcknowledgementHandler.EventReceived());
        try {
            Object decodedMessage = decode(ctx.channel(), networkMessage.getRemoteAddress(), originalMessage);
            onMessageEvent(ctx.channel(), networkMessage.getRemoteAddress(), originalMessage, decodedMessage);
            if (decodedMessage == null) {
                decodedMessage = handleEmptyMessage(ctx.channel(), networkMessage.getRemoteAddress(), originalMessage);
            }
            if (decodedMessage != null) {
                if (decodedMessage instanceof Collection collection) {
                    ctx.writeAndFlush(new AcknowledgementHandler.EventDecoded(collection));
                    for (Object o : collection) {
                        saveOriginal(o, originalMessage);
                        ctx.fireChannelRead(o);
                    }
                } else {
                    ctx.writeAndFlush(new AcknowledgementHandler.EventDecoded(List.of(decodedMessage)));
                    saveOriginal(decodedMessage, originalMessage);
                    ctx.fireChannelRead(decodedMessage);
                }
            } else {
                ctx.writeAndFlush(new AcknowledgementHandler.EventDecoded(List.of()));
            }
        } finally {
            ReferenceCountUtil.release(originalMessage);
        }
    }

    protected void onMessageEvent(
            Channel channel, SocketAddress remoteAddress, Object originalMessage, Object decodedMessage) {
    }

    protected Object handleEmptyMessage(Channel channel, SocketAddress remoteAddress, Object msg) {
        return null;
    }

    protected abstract Object decode(Channel channel, SocketAddress remoteAddress, Object msg) throws Exception;

}

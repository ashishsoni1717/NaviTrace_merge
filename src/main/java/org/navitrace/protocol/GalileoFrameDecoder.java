
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.navitrace.BaseFrameDecoder;

public class GalileoFrameDecoder extends BaseFrameDecoder {

    private static final int MESSAGE_MINIMUM_LENGTH = 6;

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        if (buf.readableBytes() < MESSAGE_MINIMUM_LENGTH) {
            return null;
        }

        int length;
        if (buf.getByte(buf.readerIndex()) == 0x01 && buf.getUnsignedMedium(buf.readerIndex() + 3) == 0x01001c) {
            length = 3 + buf.getUnsignedShort(buf.readerIndex() + 1);
        } else {
            length = 5 + (buf.getUnsignedShortLE(buf.readerIndex() + 1) & 0x7fff);
        }

        if (buf.readableBytes() >= length) {
            return buf.readRetainedSlice(length);
        }

        return null;
    }

}

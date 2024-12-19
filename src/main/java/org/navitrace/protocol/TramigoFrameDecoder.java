
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.navitrace.BaseFrameDecoder;

public class TramigoFrameDecoder extends BaseFrameDecoder {

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        if (buf.readableBytes() < 20) {
            return null;
        }

        int protocol = buf.getUnsignedByte(buf.readerIndex());

        int length;
        if (protocol == 0x80) {
            length = buf.getUnsignedShortLE(buf.readerIndex() + 6);
        } else if (protocol == 0x02 || protocol == 0x04) {
            length = buf.getUnsignedShortLE(buf.readerIndex() + 1);
        } else {
            length = buf.getUnsignedShort(buf.readerIndex() + 6);
        }

        if (length <= buf.readableBytes()) {
            return buf.readRetainedSlice(length);
        }

        return null;
    }

}

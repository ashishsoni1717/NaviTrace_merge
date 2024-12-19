
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.navitrace.BaseFrameDecoder;

public class Dsf22FrameDecoder extends BaseFrameDecoder {

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        if (buf.readableBytes() < 21) {
            return null;
        }

        int count = buf.getUnsignedByte(buf.readerIndex() + 4);

        int length = 2 + 2 + 1 + count * (4 + 4 + 4 + 1 + 2 + 1);

        if (buf.readableBytes() >= length) {
            return buf.readRetainedSlice(length);
        } else {
            return null;
        }
    }

}

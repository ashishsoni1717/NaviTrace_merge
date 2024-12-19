
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.navitrace.BaseFrameDecoder;

public class SnapperFrameDecoder extends BaseFrameDecoder {

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        byte header = buf.getByte(buf.readerIndex());
        if (header == 'P') {
            if (buf.readableBytes() >= 2) {
                return buf.readRetainedSlice(2);
            }
        } else if (buf.readableBytes() >= 16) {
            int length = buf.getIntLE(buf.readerIndex() + 12) + 12 + 4 + 9;
            if (buf.readableBytes() >= length) {
                return buf.readRetainedSlice(length);
            }
        }

        return null;
    }

}

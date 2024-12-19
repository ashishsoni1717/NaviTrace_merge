
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.navitrace.BaseFrameDecoder;

import java.nio.charset.StandardCharsets;

public class TechtoCruzFrameDecoder extends BaseFrameDecoder {

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        int lengthStart = buf.readerIndex() + 3;
        int lengthEnd = buf.indexOf(lengthStart, buf.writerIndex(), (byte) ',');
        if (lengthEnd > 0) {
            int length = lengthStart
                    + Integer.parseInt(buf.toString(lengthStart, lengthEnd - lengthStart, StandardCharsets.US_ASCII));
            if (buf.readableBytes() >= length) {
                return buf.readRetainedSlice(length);
            }
        }

        return null;
    }

}

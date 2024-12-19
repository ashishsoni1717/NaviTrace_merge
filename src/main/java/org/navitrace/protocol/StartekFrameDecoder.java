
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.navitrace.BaseFrameDecoder;

import java.nio.charset.StandardCharsets;

public class StartekFrameDecoder extends BaseFrameDecoder {

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        if (buf.readableBytes() < 10) {
            return null;
        }

        int lengthIndex = buf.readerIndex() + 3;
        int dividerIndex = buf.indexOf(lengthIndex, buf.writerIndex(), (byte) ',');
        if (dividerIndex > 0) {
            int lengthOffset = dividerIndex - buf.readerIndex() + 4;
            int length = lengthOffset + Integer.parseInt(buf.getCharSequence(
                    lengthIndex, dividerIndex - lengthIndex, StandardCharsets.US_ASCII).toString());
            if (buf.readableBytes() >= length) {
                return buf.readRetainedSlice(length);
            }
        }

        return null;
    }

}

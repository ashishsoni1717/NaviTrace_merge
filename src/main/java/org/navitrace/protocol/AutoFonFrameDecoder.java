
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.navitrace.BaseFrameDecoder;

public class AutoFonFrameDecoder extends BaseFrameDecoder {

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        // Check minimum length
        if (buf.readableBytes() < 12) {
            return null;
        }

        int length = switch (buf.getUnsignedByte(buf.readerIndex())) {
            case AutoFonProtocolDecoder.MSG_LOGIN -> 12;
            case AutoFonProtocolDecoder.MSG_LOCATION -> 78;
            case AutoFonProtocolDecoder.MSG_HISTORY -> 257;
            case AutoFonProtocolDecoder.MSG_45_LOGIN -> 19;
            case AutoFonProtocolDecoder.MSG_45_LOCATION -> 34;
            default -> 0;
        };

        // Check length and return buffer
        if (length != 0 && buf.readableBytes() >= length) {
            return buf.readRetainedSlice(length);
        }

        return null;
    }

}

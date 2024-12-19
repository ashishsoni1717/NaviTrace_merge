
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.navitrace.BaseFrameDecoder;

public class PstFrameDecoder extends BaseFrameDecoder {

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        while (buf.isReadable() && buf.getByte(buf.readerIndex()) == 0x28) {
            buf.skipBytes(1);
        }

        int endIndex = buf.indexOf(buf.readerIndex(), buf.writerIndex(), (byte) 0x29);
        if (endIndex > 0) {
            ByteBuf result = Unpooled.buffer(endIndex - buf.readerIndex());
            while (buf.readerIndex() < endIndex) {
                int b = buf.readUnsignedByte();
                if (b == 0x27) {
                    b = buf.readUnsignedByte() ^ 0x40;
                }
                result.writeByte(b);
            }
            buf.skipBytes(1);
            return result;
        }

        return null;
    }

}

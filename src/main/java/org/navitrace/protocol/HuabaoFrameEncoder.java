
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class HuabaoFrameEncoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {

        int startIndex = msg.readerIndex();
        while (msg.isReadable()) {
            int index = msg.readerIndex();
            int b = msg.readUnsignedByte();
            if (b == 0x7d) {
                out.writeByte(0x7d);
                out.writeByte(0x01);
            } else if (b == 0x7e && index != startIndex && msg.isReadable()) {
                out.writeByte(0x7d);
                out.writeByte(0x02);
            } else {
                out.writeByte(b);
            }
        }
    }
}

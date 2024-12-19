
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Xexun2FrameEncoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        out.writeBytes(msg.readBytes(2));

        while (msg.readableBytes() > 2) {
            int b = msg.readUnsignedByte();
            if (b == 0xfa && msg.isReadable() && msg.getUnsignedByte(msg.readerIndex()) == 0xaf) {
                msg.readUnsignedByte();
                out.writeByte(0xfb);
                out.writeByte(0xbf);
                out.writeByte(0x01);
            } else if (b == 0xfb && msg.isReadable() && msg.getUnsignedByte(msg.readerIndex()) == 0xbf) {
                msg.readUnsignedByte();
                out.writeByte(0xfb);
                out.writeByte(0xbf);
                out.writeByte(0x02);
            } else {
                out.writeByte(b);
            }
        }

        out.writeBytes(msg.readBytes(2));

    }
}

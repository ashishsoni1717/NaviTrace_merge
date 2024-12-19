
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.navitrace.BaseFrameDecoder;
import org.navitrace.helper.BufferUtil;

public class Xexun2FrameDecoder extends BaseFrameDecoder {

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        if (buf.readableBytes() < 5) {
            return null;
        }

        ByteBuf flag = Unpooled.wrappedBuffer(new byte[] {(byte) 0xfa, (byte) 0xaf});
        int index;
        try {
            index = BufferUtil.indexOf(flag, buf, buf.readerIndex() + 2, buf.writerIndex());
        } finally {
            flag.release();
        }

        if (index >= 0) {
            ByteBuf result = Unpooled.buffer(index + 2 - buf.readerIndex());

            while (buf.readerIndex() < index + 2) {
                int b = buf.readUnsignedByte();
                if (b == 0xfb && buf.isReadable() && buf.getUnsignedByte(buf.readerIndex()) == 0xbf) {
                    buf.readUnsignedByte(); // skip
                    int ext = buf.readUnsignedByte();
                    if (ext == 0x01) {
                        result.writeByte(0xfa);
                        result.writeByte(0xaf);
                    } else if (ext == 0x02) {
                        result.writeByte(0xfb);
                        result.writeByte(0xbf);
                    }
                } else {
                    result.writeByte(b);
                }
            }

            return result;
        }

        return null;
    }

}

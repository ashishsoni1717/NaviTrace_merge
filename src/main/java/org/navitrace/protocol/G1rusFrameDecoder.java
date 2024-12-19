
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.navitrace.BaseFrameDecoder;

public class G1rusFrameDecoder extends BaseFrameDecoder {

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ByteBuf buf) throws Exception {

        ByteBuf result = Unpooled.buffer();

        while (buf.isReadable()) {
            int b = buf.readUnsignedByte();
            if (b == 0x1B) {
                int ext = buf.readUnsignedByte();
                if (ext == 0x00) {
                    result.writeByte(0x1B);
                } else {
                    result.writeByte(0xF8);
                }
            } else {
                result.writeByte(b);
            }
        }

        return result;
    }

}

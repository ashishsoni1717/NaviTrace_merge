
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.Protocol;
import org.navitrace.model.Command;

import java.nio.charset.StandardCharsets;

public class TopinProtocolEncoder extends BaseProtocolEncoder {

    public TopinProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private ByteBuf encodeContent(int type, ByteBuf content) {

        ByteBuf buf = Unpooled.buffer();

        buf.writeByte(0x78);
        buf.writeByte(0x78);

        buf.writeByte(1 + content.readableBytes()); // message length

        buf.writeByte(type);

        buf.writeBytes(content);
        content.release();

        buf.writeByte('\r');
        buf.writeByte('\n');

        return buf;
    }

    @Override
    protected Object encodeCommand(Command command) {

        ByteBuf content = Unpooled.buffer();

        return switch (command.getType()) {
            case Command.TYPE_SOS_NUMBER -> {
                content.writeCharSequence(command.getString(Command.KEY_PHONE), StandardCharsets.US_ASCII);
                yield encodeContent(TopinProtocolDecoder.MSG_SOS_NUMBER, content);
            }
            default -> null;
        };
    }

}

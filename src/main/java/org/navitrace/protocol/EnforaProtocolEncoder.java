
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

import java.nio.charset.StandardCharsets;

public class EnforaProtocolEncoder extends StringProtocolEncoder {

    public EnforaProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private ByteBuf encodeContent(String content) {

        ByteBuf buf = Unpooled.buffer();

        buf.writeShort(content.length() + 6);
        buf.writeShort(0); // index
        buf.writeByte(0x04); // command type
        buf.writeByte(0); // optional header
        buf.writeBytes(content.getBytes(StandardCharsets.US_ASCII));

        return buf;
    }

    @Override
    protected Object encodeCommand(Command command) {
        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> encodeContent(command.getString(Command.KEY_DATA));
            case Command.TYPE_ENGINE_STOP -> encodeContent("AT$IOGP3=1");
            case Command.TYPE_ENGINE_RESUME -> encodeContent("AT$IOGP3=0");
            default -> null;
        };
    }

}

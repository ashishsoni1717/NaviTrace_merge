
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

import java.nio.charset.StandardCharsets;

public class NoranProtocolEncoder extends BaseProtocolEncoder {

    public NoranProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private ByteBuf encodeContent(String content) {

        ByteBuf buf = Unpooled.buffer(12 + 56);

        buf.writeCharSequence("\r\n*KW", StandardCharsets.US_ASCII);
        buf.writeByte(0);
        buf.writeShortLE(buf.capacity());
        buf.writeShortLE(NoranProtocolDecoder.MSG_CONTROL);
        buf.writeInt(0); // gis ip
        buf.writeShortLE(0); // gis port
        buf.writeBytes(content.getBytes(StandardCharsets.US_ASCII));
        buf.writerIndex(buf.writerIndex() + 50 - content.length());
        buf.writeCharSequence("\r\n", StandardCharsets.US_ASCII);

        return buf;
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_POSITION_SINGLE -> encodeContent("*KW,000,000,000000#");
            case Command.TYPE_POSITION_PERIODIC -> {
                int interval = command.getInteger(Command.KEY_FREQUENCY);
                yield encodeContent("*KW,000,002,000000," + interval + "#");
            }
            case Command.TYPE_POSITION_STOP -> encodeContent("*KW,000,002,000000,0#");
            case Command.TYPE_ENGINE_STOP -> encodeContent("*KW,000,007,000000,0#");
            case Command.TYPE_ENGINE_RESUME -> encodeContent("*KW,000,007,000000,1#");
            default -> null;
        };
    }

}


package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.helper.Checksum;
import org.navitrace.helper.DataConverter;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

import java.nio.charset.StandardCharsets;

public class Xexun2ProtocolEncoder extends BaseProtocolEncoder {

    public Xexun2ProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private static ByteBuf encodeContent(String uniqueId, String content) {
        ByteBuf buf = Unpooled.buffer();

        ByteBuf message = Unpooled.copiedBuffer(content.getBytes(StandardCharsets.US_ASCII));

        buf.writeShort(Xexun2ProtocolDecoder.FLAG);
        buf.writeShort(Xexun2ProtocolDecoder.MSG_COMMAND);
        buf.writeShort(1); // index
        buf.writeBytes(DataConverter.parseHex(uniqueId + "0"));
        buf.writeShort(message.readableBytes());
        buf.writeShort(Checksum.ip(message.nioBuffer()));
        buf.writeBytes(message);
        buf.writeShort(Xexun2ProtocolDecoder.FLAG);

        return buf;
    }

    @Override
    protected Object encodeCommand(Command command) {
        String uniqueId = getUniqueId(command.getDeviceId());

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> encodeContent(uniqueId, command.getString(Command.KEY_DATA));
            case Command.TYPE_POSITION_PERIODIC -> encodeContent(
                    uniqueId, String.format("tracking_send=%1$d,%1$d", command.getInteger(Command.KEY_FREQUENCY)));
            case Command.TYPE_POWER_OFF -> encodeContent(uniqueId, "of=1");
            case Command.TYPE_REBOOT_DEVICE -> encodeContent(uniqueId, "reset");
            default -> null;
        };
    }

}

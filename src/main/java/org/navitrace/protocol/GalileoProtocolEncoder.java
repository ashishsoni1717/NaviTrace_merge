
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.helper.Checksum;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

import java.nio.charset.StandardCharsets;

public class GalileoProtocolEncoder extends BaseProtocolEncoder {

    public GalileoProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private ByteBuf encodeText(String uniqueId, String text) {

        ByteBuf buf = Unpooled.buffer(256);

        buf.writeByte(0x01);
        buf.writeShortLE(uniqueId.length() + text.length() + 11);

        buf.writeByte(0x03); // imei tag
        buf.writeBytes(uniqueId.getBytes(StandardCharsets.US_ASCII));

        buf.writeByte(0x04); // device id tag
        buf.writeShortLE(0); // not needed if imei provided

        buf.writeByte(0xE0); // index tag
        buf.writeIntLE(0); // index

        buf.writeByte(0xE1); // command text tag
        buf.writeByte(text.length());
        buf.writeBytes(text.getBytes(StandardCharsets.US_ASCII));

        buf.writeShortLE(Checksum.crc16(Checksum.CRC16_MODBUS, buf.nioBuffer(0, buf.writerIndex())));

        return buf;
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> encodeText(getUniqueId(command.getDeviceId()),
                    command.getString(Command.KEY_DATA));
            case Command.TYPE_OUTPUT_CONTROL -> encodeText(getUniqueId(command.getDeviceId()),
                    "Out " + command.getInteger(Command.KEY_INDEX) + "," + command.getString(Command.KEY_DATA));
            default -> null;
        };
    }

}

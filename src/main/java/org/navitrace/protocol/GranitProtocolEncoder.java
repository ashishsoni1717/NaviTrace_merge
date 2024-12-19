
package org.navitrace.protocol;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class GranitProtocolEncoder extends BaseProtocolEncoder {

    public GranitProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private ByteBuf encodeCommand(String commandString) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(commandString.getBytes(StandardCharsets.US_ASCII));
        GranitProtocolDecoder.appendChecksum(buffer, commandString.length());
        return buffer;
    }

    @Override
    protected Object encodeCommand(Command command) {
        return switch (command.getType()) {
            case Command.TYPE_IDENTIFICATION -> encodeCommand("BB+IDNT");
            case Command.TYPE_REBOOT_DEVICE -> encodeCommand("BB+RESET");
            case Command.TYPE_POSITION_SINGLE -> encodeCommand("BB+RRCD");
            default -> null;
        };
    }

}

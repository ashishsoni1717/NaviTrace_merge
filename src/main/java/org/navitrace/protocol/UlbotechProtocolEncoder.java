
package org.navitrace.protocol;

import io.netty.buffer.Unpooled;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.Protocol;
import org.navitrace.model.Command;

import java.nio.charset.StandardCharsets;

public class UlbotechProtocolEncoder extends BaseProtocolEncoder {

    public UlbotechProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {
        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> Unpooled.copiedBuffer(
                    "*TS01," + command.getString(Command.KEY_DATA) + "#", StandardCharsets.US_ASCII);
            default -> null;
        };
    }

}

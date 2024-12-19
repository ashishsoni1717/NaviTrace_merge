
package org.navitrace.protocol;

import io.netty.buffer.Unpooled;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

import java.nio.charset.StandardCharsets;

public class AtrackProtocolEncoder extends BaseProtocolEncoder {

    public AtrackProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        if (command.getType().equals(Command.TYPE_CUSTOM)) {
            return Unpooled.copiedBuffer(
                    command.getString(Command.KEY_DATA) + "\r\n", StandardCharsets.US_ASCII);
        }
        return null;
    }

}

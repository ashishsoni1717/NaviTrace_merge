
package org.navitrace.protocol;

import org.navitrace.BaseProtocolEncoder;
import org.navitrace.Protocol;
import org.navitrace.model.Command;

public class NdtpV6ProtocolEncoder extends BaseProtocolEncoder {

    public NdtpV6ProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {
        return switch (command.getType()) {
            case Command.TYPE_IDENTIFICATION -> "BB+IDNT";
            case Command.TYPE_REBOOT_DEVICE -> "BB+RESET";
            case Command.TYPE_POSITION_SINGLE -> "BB+RRCD";
            default -> null;
        };
    }

}

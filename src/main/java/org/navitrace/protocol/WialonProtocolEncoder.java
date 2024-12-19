
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class WialonProtocolEncoder extends StringProtocolEncoder {

    public WialonProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {
        return switch (command.getType()) {
            case Command.TYPE_REBOOT_DEVICE -> formatCommand(command, "reboot\r\n");
            case Command.TYPE_SEND_USSD -> formatCommand(command, "USSD:%s\r\n", Command.KEY_PHONE);
            case Command.TYPE_IDENTIFICATION -> formatCommand(command, "VER?\r\n");
            case Command.TYPE_OUTPUT_CONTROL ->
                    formatCommand(command, "L%s=%s\r\n", Command.KEY_INDEX, Command.KEY_DATA);
            default -> null;
        };
    }

}

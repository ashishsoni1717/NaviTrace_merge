
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class GranitProtocolSmsEncoder extends StringProtocolEncoder {

    public GranitProtocolSmsEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected String encodeCommand(Command command) {
        return switch (command.getType()) {
            case Command.TYPE_REBOOT_DEVICE -> "BB+RESET";
            case Command.TYPE_POSITION_PERIODIC -> formatCommand(command, "BB+BBMD=%s", Command.KEY_FREQUENCY);
            default -> null;
        };
    }

}

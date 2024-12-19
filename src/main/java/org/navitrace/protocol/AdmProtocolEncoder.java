
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class AdmProtocolEncoder extends StringProtocolEncoder {

    public AdmProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_GET_DEVICE_STATUS -> formatCommand(command, "STATUS\r\n");
            case Command.TYPE_CUSTOM -> formatCommand(command, "%s\r\n", Command.KEY_DATA);
            default -> null;
        };
    }

}

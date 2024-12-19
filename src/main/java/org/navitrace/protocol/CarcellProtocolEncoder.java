
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class CarcellProtocolEncoder extends StringProtocolEncoder {

    public CarcellProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_ENGINE_STOP -> formatCommand(command, "$SRVCMD,%s,BA#\r\n", Command.KEY_UNIQUE_ID);
            case Command.TYPE_ENGINE_RESUME -> formatCommand(command, "$SRVCMD,%s,BD#\r\n", Command.KEY_UNIQUE_ID);
            default -> null;
        };
    }

}


package org.navitrace.protocol;

import org.navitrace.Protocol;
import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;

public class PortmanProtocolEncoder extends StringProtocolEncoder {

    public PortmanProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_ENGINE_STOP -> formatCommand(command, "&&%s,XA5\r\n", Command.KEY_UNIQUE_ID);
            case Command.TYPE_ENGINE_RESUME -> formatCommand(command, "&&%s,XA6\r\n", Command.KEY_UNIQUE_ID);
            default -> null;
        };
    }

}

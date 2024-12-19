
package org.navitrace.protocol;

import org.navitrace.Protocol;
import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;

public class EasyTrackProtocolEncoder extends StringProtocolEncoder {

    public EasyTrackProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_ENGINE_STOP -> formatCommand(command, "*ET,%s,FD,Y1#", Command.KEY_UNIQUE_ID);
            case Command.TYPE_ENGINE_RESUME -> formatCommand(command, "*ET,%s,FD,Y2#", Command.KEY_UNIQUE_ID);
            case Command.TYPE_ALARM_ARM -> formatCommand(command, "*ET,%s,FD,F1#", Command.KEY_UNIQUE_ID);
            case Command.TYPE_ALARM_DISARM -> formatCommand(command, "*ET,%s,FD,F2#", Command.KEY_UNIQUE_ID);
            default -> null;
        };
    }

}

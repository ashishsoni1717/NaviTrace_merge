
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class SviasProtocolEncoder extends StringProtocolEncoder {

    public SviasProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {
        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> formatCommand(command, "%s", Command.KEY_DATA);
            case Command.TYPE_POSITION_SINGLE -> formatCommand(command, "AT+STR=1*");
            case Command.TYPE_SET_ODOMETER -> formatCommand(command, "AT+ODT=%s*", Command.KEY_DATA);
            case Command.TYPE_ENGINE_STOP -> formatCommand(command, "AT+OUT=1,1*");
            case Command.TYPE_ENGINE_RESUME -> formatCommand(command, "AT+OUT=1,0*");
            case Command.TYPE_ALARM_ARM -> formatCommand(command, "AT+OUT=2,1*");
            case Command.TYPE_ALARM_DISARM -> formatCommand(command, "AT+OUT=2,0*");
            case Command.TYPE_ALARM_REMOVE -> formatCommand(command, "AT+PNC=600*");
            default -> null;
        };
    }

}

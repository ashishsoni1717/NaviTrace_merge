
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class Gl200ProtocolEncoder extends StringProtocolEncoder {

    public Gl200ProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        initDevicePassword(command, "");

        return switch (command.getType()) {
            case Command.TYPE_POSITION_SINGLE -> formatCommand(
                    command, "AT+GTRTO=%s,1,,,,,,FFFF$", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_ENGINE_STOP -> formatCommand(
                    command, "AT+GTOUT=%s,1,,,0,0,0,0,0,0,0,,,,,,,FFFF$", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_ENGINE_RESUME -> formatCommand(
                    command, "AT+GTOUT=%s,0,,,0,0,0,0,0,0,0,,,,,,,FFFF$", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_IDENTIFICATION -> formatCommand(
                    command, "AT+GTRTO=%s,8,,,,,,FFFF$", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_REBOOT_DEVICE -> formatCommand(
                    command, "AT+GTRTO=%s,3,,,,,,FFFF$", Command.KEY_DEVICE_PASSWORD);
            default -> null;
        };
    }

}

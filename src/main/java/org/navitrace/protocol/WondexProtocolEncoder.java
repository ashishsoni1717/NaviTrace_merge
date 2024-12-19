
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class WondexProtocolEncoder extends StringProtocolEncoder {

    public WondexProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        initDevicePassword(command, "0000");

        return switch (command.getType()) {
            case Command.TYPE_REBOOT_DEVICE -> formatCommand(command, "$WP+REBOOT=%s", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_GET_DEVICE_STATUS -> formatCommand(command, "$WP+TEST=%s", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_GET_MODEM_STATUS -> formatCommand(command, "$WP+GSMINFO=%s", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_IDENTIFICATION -> formatCommand(command, "$WP+IMEI=%s", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_POSITION_SINGLE ->
                    formatCommand(command, "$WP+GETLOCATION=%s", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_GET_VERSION -> formatCommand(command, "$WP+VER=%s", Command.KEY_DEVICE_PASSWORD);
            default -> null;
        };
    }

}

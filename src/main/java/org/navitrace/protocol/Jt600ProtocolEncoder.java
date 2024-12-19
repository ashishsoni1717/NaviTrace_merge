
package org.navitrace.protocol;

import java.util.TimeZone;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class Jt600ProtocolEncoder extends StringProtocolEncoder {

    public Jt600ProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_ENGINE_STOP -> "(S07,0)";
            case Command.TYPE_ENGINE_RESUME -> "(S07,1)";
            case Command.TYPE_SET_TIMEZONE -> {
                int offset = TimeZone.getTimeZone(command.getString(Command.KEY_TIMEZONE)).getRawOffset() / 60000;
                yield "(S09,1," + offset + ")";
            }
            case Command.TYPE_REBOOT_DEVICE -> "(S17)";
            default -> null;
        };
    }

}

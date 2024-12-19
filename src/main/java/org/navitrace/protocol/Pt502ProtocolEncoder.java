
package org.navitrace.protocol;

import java.util.TimeZone;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class Pt502ProtocolEncoder extends StringProtocolEncoder implements StringProtocolEncoder.ValueFormatter {

    public Pt502ProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    public String formatValue(String key, Object value) {
        if (key.equals(Command.KEY_TIMEZONE)) {
            return String.valueOf(TimeZone.getTimeZone((String) value).getRawOffset() / 3600000);
        }

        return null;
    }

    @Override
    protected String formatCommand(Command command, String format, String... keys) {
        return formatCommand(command, format, this, keys);
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> formatCommand(command, "%s\r\n", Command.KEY_DATA);
            case Command.TYPE_OUTPUT_CONTROL ->
                    formatCommand(command, "#OPC%s,%s\r\n", Command.KEY_INDEX, Command.KEY_DATA);
            case Command.TYPE_SET_TIMEZONE -> formatCommand(command, "#TMZ%s\r\n", Command.KEY_TIMEZONE);
            case Command.TYPE_ALARM_SPEED -> formatCommand(command, "#SPD%s\r\n", Command.KEY_DATA);
            case Command.TYPE_REQUEST_PHOTO -> formatCommand(command, "#PHO\r\n");
            default -> null;
        };
    }

}

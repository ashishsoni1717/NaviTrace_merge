
package org.navitrace.protocol;

import org.navitrace.Protocol;
import org.navitrace.StringProtocolEncoder;
import org.navitrace.config.Keys;
import org.navitrace.helper.model.AttributeUtil;
import org.navitrace.model.Command;

import java.util.Date;

public class H02ProtocolEncoder extends StringProtocolEncoder {

    private static final String MARKER = "HQ";

    public H02ProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private Object formatCommand(Date time, String uniqueId, String type, String... params) {

        StringBuilder result = new StringBuilder(
                String.format("*%s,%s,%s,%4$tH%4$tM%4$tS", MARKER, uniqueId, type, time));

        for (String param : params) {
            result.append(",").append(param);
        }

        result.append("#");

        return result.toString();
    }

    protected Object encodeCommand(Command command, Date time) {
        String uniqueId = getUniqueId(command.getDeviceId());

        return switch (command.getType()) {
            case Command.TYPE_ALARM_ARM -> formatCommand(time, uniqueId, "SCF", "0", "0");
            case Command.TYPE_ALARM_DISARM -> formatCommand(time, uniqueId, "SCF", "1", "1");
            case Command.TYPE_ENGINE_STOP -> formatCommand(time, uniqueId, "S20", "1", "1");
            case Command.TYPE_ENGINE_RESUME -> formatCommand(time, uniqueId, "S20", "1", "0");
            case Command.TYPE_POSITION_PERIODIC -> {
                String frequency = command.getAttributes().get(Command.KEY_FREQUENCY).toString();
                if (AttributeUtil.lookup(
                        getCacheManager(), Keys.PROTOCOL_ALTERNATIVE.withPrefix(getProtocolName()),
                        command.getDeviceId())) {
                    yield formatCommand(time, uniqueId, "D1", frequency);
                } else {
                    yield formatCommand(time, uniqueId, "S71", "22", frequency);
                }
            }
            default -> null;
        };
    }

    @Override
    protected Object encodeCommand(Command command) {
        return encodeCommand(command, new Date());
    }

}

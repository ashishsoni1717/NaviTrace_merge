
package org.navitrace.protocol;

import org.navitrace.Protocol;
import org.navitrace.StringProtocolEncoder;
import org.navitrace.config.Keys;
import org.navitrace.helper.Checksum;
import org.navitrace.helper.model.AttributeUtil;
import org.navitrace.model.Command;

public class MeitrackProtocolEncoder extends StringProtocolEncoder {

    public MeitrackProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private Object formatCommand(Command command, String content) {
        String uniqueId = getUniqueId(command.getDeviceId());
        int length = 1 + uniqueId.length() + 1 + content.length() + 5;
        String result = String.format("@@A%02d,%s,%s*", length, uniqueId, content);
        result += Checksum.sum(result) + "\r\n";
        return result;
    }

    @Override
    protected Object encodeCommand(Command command) {

        boolean alternative = AttributeUtil.lookup(
                getCacheManager(), Keys.PROTOCOL_ALTERNATIVE.withPrefix(getProtocolName()), command.getDeviceId());

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> formatCommand(command, command.getString(Command.KEY_DATA));
            case Command.TYPE_POSITION_SINGLE -> formatCommand(command, "A10");
            case Command.TYPE_ENGINE_STOP -> formatCommand(command, "C01,0,12222");
            case Command.TYPE_ENGINE_RESUME -> formatCommand(command, "C01,0,02222");
            case Command.TYPE_ALARM_ARM -> formatCommand(command, alternative ? "B21,1" : "C01,0,22122");
            case Command.TYPE_ALARM_DISARM -> formatCommand(command, alternative ? "B21,0" : "C01,0,22022");
            case Command.TYPE_REQUEST_PHOTO -> {
                int index = command.getInteger(Command.KEY_INDEX);
                yield formatCommand(command, "D03," + (index > 0 ? index : 1) + ",camera_picture.jpg");
            }
            case Command.TYPE_SEND_SMS -> {
                String phone = command.getString(Command.KEY_PHONE);
                String message = command.getString(Command.KEY_MESSAGE);
                yield formatCommand(command, "C02,0," + phone + "," + message);
            }
            default -> null;
        };
    }

}

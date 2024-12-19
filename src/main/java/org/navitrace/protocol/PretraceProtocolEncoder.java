
package org.navitrace.protocol;

import org.navitrace.BaseProtocolEncoder;
import org.navitrace.Protocol;
import org.navitrace.helper.Checksum;
import org.navitrace.model.Command;

public class PretraceProtocolEncoder extends BaseProtocolEncoder {

    public PretraceProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private String formatCommand(String uniqueId, String data) {
        String content = uniqueId + data;
        return String.format("(%s^%02X)", content, Checksum.xor(content));
    }

    @Override
    protected Object encodeCommand(Command command) {

        String uniqueId = getUniqueId(command.getDeviceId());

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> formatCommand(
                    uniqueId, command.getString(Command.KEY_DATA));
            case Command.TYPE_POSITION_PERIODIC -> formatCommand(
                    uniqueId, String.format("D221%1$d,%1$d,,", command.getInteger(Command.KEY_FREQUENCY)));
            default -> null;
        };
    }

}

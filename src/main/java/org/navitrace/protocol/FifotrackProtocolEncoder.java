
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.helper.Checksum;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class FifotrackProtocolEncoder extends StringProtocolEncoder {

    public FifotrackProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private Object formatCommand(Command command, String content) {
        String uniqueId = getUniqueId(command.getDeviceId());
        int length = 1 + uniqueId.length() + 3 + content.length();
        String result = String.format("##%02d,%s,1,%s*", length, uniqueId, content);
        result += Checksum.sum(result) + "\r\n";
        return result;
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> formatCommand(command, command.getString(Command.KEY_DATA));
            case Command.TYPE_REQUEST_PHOTO -> formatCommand(command, "D05,3");
            default -> null;
        };
    }

}

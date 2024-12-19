
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.Protocol;
import org.navitrace.StringProtocolEncoder;
import org.navitrace.helper.Checksum;
import org.navitrace.model.Command;

public class StartekProtocolEncoder extends StringProtocolEncoder {

    public StartekProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected String formatCommand(Command command, String format, String... keys) {
        String uniqueId = getUniqueId(command.getDeviceId());
        String payload = super.formatCommand(command, format, keys);
        int length = 1 + uniqueId.length() + 1 + payload.length();
        String sentence = "$$:" + length + "," + uniqueId + "," + payload;
        return sentence + Checksum.sum(sentence) + "\r\n";
    }

    @Override
    protected Object encodeCommand(Channel channel, Command command) {

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> formatCommand(command, "%s", Command.KEY_DATA);
            case Command.TYPE_OUTPUT_CONTROL ->
                    formatCommand(command, "900,%s,%s", Command.KEY_INDEX, Command.KEY_DATA);
            case Command.TYPE_ENGINE_STOP -> formatCommand(command, "900,1,1");
            case Command.TYPE_ENGINE_RESUME -> formatCommand(command, "900,1,0");
            default -> null;
        };
    }

}

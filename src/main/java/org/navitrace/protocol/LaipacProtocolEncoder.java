
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.helper.Checksum;
import org.navitrace.Protocol;

public class LaipacProtocolEncoder extends StringProtocolEncoder {

    public LaipacProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected String formatCommand(Command command, String format, String... keys) {
        String sentence = super.formatCommand(command, "$" + format, keys);
        sentence += Checksum.nmea(sentence.substring(1)) + "\r\n";
        return sentence;
    }

    @Override
    protected Object encodeCommand(Command command) {

        initDevicePassword(command, LaipacProtocolDecoder.DEFAULT_DEVICE_PASSWORD);

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> formatCommand(
                    command, "%s", Command.KEY_DATA);
            case Command.TYPE_POSITION_SINGLE -> formatCommand(
                    command, "AVREQ,%s,1", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_REBOOT_DEVICE -> formatCommand(
                    command, "AVRESET,%s,%s", Command.KEY_UNIQUE_ID, Command.KEY_DEVICE_PASSWORD);
            default -> null;
        };

    }

}

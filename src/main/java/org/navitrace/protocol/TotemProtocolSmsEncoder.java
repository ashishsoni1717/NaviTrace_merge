
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class TotemProtocolSmsEncoder extends StringProtocolEncoder {

    public TotemProtocolSmsEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        initDevicePassword(command, "000000");

        return String.format("*%s#", TotemProtocolEncoder.formatContent(command));
    }

}

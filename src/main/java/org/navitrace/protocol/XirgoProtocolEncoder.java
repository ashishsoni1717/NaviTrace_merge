
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class XirgoProtocolEncoder extends StringProtocolEncoder {

    public XirgoProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_OUTPUT_CONTROL ->
                    String.format("+XT:7005,%d,1", command.getInteger(Command.KEY_DATA) + 1);
            default -> null;
        };
    }

}

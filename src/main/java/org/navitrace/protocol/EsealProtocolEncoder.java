
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class EsealProtocolEncoder extends StringProtocolEncoder {

    public EsealProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> formatCommand(
                    command, "##S,eSeal,%s,256,3.0.8,%s,E##", Command.KEY_UNIQUE_ID, Command.KEY_DATA);
            case Command.TYPE_ALARM_ARM -> formatCommand(
                    command, "##S,eSeal,%s,256,3.0.8,RC-Power Control,Power OFF,E##", Command.KEY_UNIQUE_ID);
            case Command.TYPE_ALARM_DISARM -> formatCommand(
                    command, "##S,eSeal,%s,256,3.0.8,RC-Unlock,E##", Command.KEY_UNIQUE_ID);
            default -> null;
        };
    }

}

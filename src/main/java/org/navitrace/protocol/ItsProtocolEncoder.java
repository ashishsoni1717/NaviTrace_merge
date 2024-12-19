
package org.navitrace.protocol;

import org.navitrace.StringProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class ItsProtocolEncoder extends StringProtocolEncoder {

    public ItsProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {
        return switch (command.getType()) {
            case Command.TYPE_ENGINE_STOP -> "@SET#RLP,OP1,";
            case Command.TYPE_ENGINE_RESUME -> "@CLR#RLP,OP1,";
            default -> null;
        };
    }

}

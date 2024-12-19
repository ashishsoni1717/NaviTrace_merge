
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.model.Command;
import org.navitrace.Protocol;

public class Xrb28ProtocolEncoder extends BaseProtocolEncoder {

    public Xrb28ProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private String formatCommand(Command command, String content) {
        return String.format("\u00ff\u00ff*SCOS,OM,%s,%s#\n", getUniqueId(command.getDeviceId()), content);
    }

    @Override
    protected Object encodeCommand(Channel channel, Command command) {

        return switch (command.getType()) {
            case Command.TYPE_CUSTOM -> formatCommand(command, command.getString(Command.KEY_DATA));
            case Command.TYPE_POSITION_SINGLE -> formatCommand(command, "D0");
            case Command.TYPE_POSITION_PERIODIC ->
                    formatCommand(command, "D1," + command.getInteger(Command.KEY_FREQUENCY));
            case Command.TYPE_ENGINE_STOP, Command.TYPE_ALARM_DISARM -> {
                if (channel != null) {
                    Xrb28ProtocolDecoder decoder = channel.pipeline().get(Xrb28ProtocolDecoder.class);
                    if (decoder != null) {
                        decoder.setPendingCommand(command.getType());
                    }
                }
                yield formatCommand(command, "R0,0,20,1234," + System.currentTimeMillis() / 1000);
            }
            default -> null;
        };
    }

}


package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.session.DeviceSession;
import org.navitrace.NetworkMessage;
import org.navitrace.Protocol;
import org.navitrace.helper.Checksum;

import java.net.SocketAddress;

public class R12wProtocolDecoder extends BaseProtocolDecoder {

    public R12wProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private void sendResponse(Channel channel, String type, String id, String data) {
        if (channel != null) {
            String sentence = String.format("$HX,%s,%s,%s,#", type, id, data);
            sentence += String.format(",%02x,\r\n", Checksum.xor(sentence));
            channel.writeAndFlush(new NetworkMessage(sentence, channel.remoteAddress()));
        }
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;
        String[] values = sentence.split(",");
        String type = values[1];
        String id = values[2];

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, id);
        if (deviceSession == null) {
            return null;
        }

        if (type.equals("0001")) {
            sendResponse(channel, "1001", id, values[3] + ",OK");
        }

        return null;
    }

}

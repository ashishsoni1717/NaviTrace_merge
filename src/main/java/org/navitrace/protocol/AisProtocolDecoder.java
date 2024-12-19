
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.session.DeviceSession;
import org.navitrace.Protocol;
import org.navitrace.helper.BitBuffer;
import org.navitrace.helper.Parser;
import org.navitrace.helper.PatternBuilder;
import org.navitrace.model.Position;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AisProtocolDecoder extends BaseProtocolDecoder {

    public AisProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("!AIVDM,")
            .number("(d+),")                     // count
            .number("(d+),")                     // index
            .number("(d+)?,")                    // id
            .expression(".,")                    // radio channel
            .expression("([^,]+),")              // payload
            .any()
            .compile();

    private Position decodePayload(Channel channel, SocketAddress remoteAddress, BitBuffer buf) {

        int type = buf.readUnsigned(6);
        if (type == 1 || type == 2 || type == 3 || type == 18) {

            buf.readUnsigned(2);
            int mmsi = buf.readUnsigned(30);

            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, String.valueOf(mmsi));
            if (deviceSession == null) {
                return null;
            }

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            position.setTime(new Date());

            if (type == 18) {
                buf.readUnsigned(8); // reserved
            } else {
                position.set(Position.KEY_STATUS, buf.readUnsigned(4));
                position.set("turn", buf.readSigned(8));
            }

            position.setSpeed(buf.readUnsigned(10) * 0.1);
            position.setValid(buf.readUnsigned(1) != 0);
            position.setLongitude(buf.readSigned(28) * 0.0001 / 60.0);
            position.setLatitude(buf.readSigned(27) * 0.0001 / 60.0);
            position.setCourse(buf.readUnsigned(12) * 0.1);

            position.set("heading", buf.readUnsigned(9));

            return position;

        }

        return null;
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String[] sentences = ((String) msg).split("\\r\\n");

        List<Position> positions = new ArrayList<>();
        Map<Integer, BitBuffer> buffers = new HashMap<>();

        for (String sentence : sentences) {
            if (!sentence.isEmpty()) {
                Parser parser = new Parser(PATTERN, sentence);
                if (parser.matches()) {

                    int count = parser.nextInt(0);
                    int index = parser.nextInt(0);
                    int id = parser.nextInt(0);

                    Position position = null;

                    if (count == 1) {
                        BitBuffer bits = new BitBuffer();
                        bits.writeEncoded(parser.next().getBytes(StandardCharsets.US_ASCII));
                        position = decodePayload(channel, remoteAddress, bits);
                    } else {
                        BitBuffer bits = buffers.get(id);
                        if (bits == null) {
                            bits = new BitBuffer();
                            buffers.put(id, bits);
                        }
                        bits.writeEncoded(parser.next().getBytes(StandardCharsets.US_ASCII));
                        if (count == index) {
                            position = decodePayload(channel, remoteAddress, bits);
                            buffers.remove(id);
                        }
                    }

                    if (position != null) {
                        positions.add(position);
                    }

                }
            }
        }

        return positions;
    }

}

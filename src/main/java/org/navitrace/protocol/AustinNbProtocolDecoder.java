
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.session.DeviceSession;
import org.navitrace.Protocol;
import org.navitrace.helper.Parser;
import org.navitrace.helper.PatternBuilder;
import org.navitrace.model.Position;

import java.net.SocketAddress;
import java.util.regex.Pattern;

public class AustinNbProtocolDecoder extends BaseProtocolDecoder {

    public AustinNbProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .number("(d+);")                     // imei
            .number("(dddd)-(dd)-(dd) ")         // date
            .number("(dd):(dd):(dd);")           // time
            .number("(-?d+,d+);")                // latitude
            .number("(-?d+,d+);")                // longitude
            .number("(d+);")                     // azimuth
            .number("(d+);")                     // angle
            .number("(d+);")                     // range
            .number("(d+);")                     // out of range
            .expression("(.*)")                  // operator
            .any()
            .compile();

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        Parser parser = new Parser(PATTERN, (String) msg);
        if (!parser.matches()) {
            return null;
        }

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        position.setTime(parser.nextDateTime());

        position.setValid(true);
        position.setLatitude(Double.parseDouble(parser.next().replace(',', '.')));
        position.setLongitude(Double.parseDouble(parser.next().replace(',', '.')));
        position.setCourse(parser.nextInt());
        position.set("angle", parser.nextInt());
        position.set("range", parser.nextInt());
        position.set("outOfRange", parser.nextInt());
        position.set("carrier", parser.next());

        return position;
    }

}

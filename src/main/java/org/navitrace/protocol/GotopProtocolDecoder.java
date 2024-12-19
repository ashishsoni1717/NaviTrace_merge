
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.session.DeviceSession;
import org.navitrace.Protocol;
import org.navitrace.helper.Parser;
import org.navitrace.helper.PatternBuilder;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.model.Position;

import java.net.SocketAddress;
import java.util.regex.Pattern;

public class GotopProtocolDecoder extends BaseProtocolDecoder {

    public GotopProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .number("(d+),")                     // imei
            .expression("([^,]+),")              // type
            .expression("([AV]),")               // validity
            .text("DATE:").optional()
            .number("(dd)(dd)(dd),")             // date (yyddmm)
            .text("TIME:").optional()
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .text("LAT:").optional()
            .number("(d+.d+)([NS]),")            // latitude
            .expression("LO[NT]:").optional()
            .number("(d+.d+)([EW]),")            // longitude
            .text("Speed:").number("(d+.d+),")   // speed
            .expression("([^,]+),")              // status
            .number("(d+)?")                     // course
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

        String type = parser.next();
        if (type.equals("CMD-KEY")) {
            position.set(Position.KEY_ALARM, Position.ALARM_SOS);
        } else if (type.startsWith("ALM-B")) {
            if (Character.getNumericValue(type.charAt(5)) % 2 > 0) {
                position.set(Position.KEY_ALARM, Position.ALARM_GEOFENCE_ENTER);
            } else {
                position.set(Position.KEY_ALARM, Position.ALARM_GEOFENCE_EXIT);
            }
        }

        position.setValid(parser.next().equals("A"));

        position.setTime(parser.nextDateTime());

        position.setLatitude(parser.nextCoordinate(Parser.CoordinateFormat.DEG_HEM));
        position.setLongitude(parser.nextCoordinate(Parser.CoordinateFormat.DEG_HEM));
        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextDouble(0)));

        position.set(Position.KEY_STATUS, parser.next());

        position.setCourse(parser.nextDouble(0));

        return position;
    }

}

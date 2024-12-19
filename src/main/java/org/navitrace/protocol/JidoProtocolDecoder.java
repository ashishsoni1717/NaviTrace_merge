
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

public class JidoProtocolDecoder extends BaseProtocolDecoder {

    public JidoProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("*")
            .number("(d+),")                     // imei
            .number("(d+),")                     // command
            .expression("([AV]),").optional()    // validity
            .number("(dd)(dd)(dd),")             // date (ddmmyy)
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("(d+)(dd.d+),")              // latitude
            .expression("([NS]),")
            .number("(d+)(dd.d+),")              // longitude
            .expression("([EW]),")
            .groupBegin()
            .number("(d+),")                     // speed
            .number("(d+),")                     // odometer
            .number("(d+),")                     // course
            .number("(-?d+),")                   // altitude
            .number("(d+),")                     // satellites
            .number("d+,")                       // gsm 1
            .number("d+,")                       // gsm 2
            .number("([01]),")                   // charging
            .number("(d+),")                     // battery level
            .expression("([YKN]),")              // mode
            .number("([01]),")                   // lock
            .number("[^,]+,")                    // accelerometer x
            .number("[^,]+,")                    // accelerometer y
            .number("[^,]+,")                    // accelerometer z
            .or()
            .expression("[^,]*,")                // data
            .groupEnd()
            .number("xx")                        // checksum
            .compile();

    private String decodeAlarm(int type) {
        return switch (type) {
            case 3 -> Position.ALARM_LOW_BATTERY;
            case 4 -> Position.ALARM_TAMPERING;
            default -> null;
        };
    }

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

        position.set(Position.KEY_ALARM, decodeAlarm(parser.nextInt()));

        if (parser.hasNext()) {
            position.setValid(parser.next().equals("A"));
        } else {
            position.setValid(true);
        }

        position.setTime(parser.nextDateTime(Parser.DateTimeFormat.DMY_HMS));
        position.setLatitude(parser.nextCoordinate());
        position.setLongitude(parser.nextCoordinate());

        if (parser.hasNext(9)) {

            position.setSpeed(UnitsConverter.knotsFromKph(parser.nextInt()));

            position.set(Position.KEY_ODOMETER, parser.nextInt());

            position.setCourse(parser.nextInt());
            position.setAltitude(parser.nextInt());

            position.set(Position.KEY_SATELLITES, parser.nextInt());
            position.set(Position.KEY_CHARGE, parser.nextInt() > 0);
            position.set(Position.KEY_BATTERY_LEVEL, parser.nextInt());
            position.set("mode", parser.next());
            position.set(Position.KEY_BLOCKED, parser.nextInt() > 0);

        }

        return position;
    }

}

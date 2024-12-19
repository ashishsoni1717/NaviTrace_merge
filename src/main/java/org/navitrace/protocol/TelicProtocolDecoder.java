
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

public class TelicProtocolDecoder extends BaseProtocolDecoder {

    public TelicProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .number("dddd")
            .number("(d{6}|d{15})")              // device id
            .number("(d{1,2}),")                 // type
            .number("d{12},")                    // event time
            .number("d+,")
            .number("(dd)(dd)(dd)")              // date (ddmmyy)
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .groupBegin()
            .number("(-?d{8,}),")                // longitude
            .number("(-?d{7,}),")                // latitude
            .or()
            .number("(-?d+),")                   // longitude
            .number("(-?d+),")                   // latitude
            .groupEnd()
            .number("(d),")                      // validity
            .number("(d+),")                     // speed
            .number("(d+),")                     // course
            .number("(d+)?,")                    // satellites
            .expression("(?:[^,]*,){7}")
            .number("(d+),")                     // battery
            .any()
            .compile();

    private String decodeAlarm(int eventId) {
        return switch (eventId) {
            case 1 -> Position.ALARM_POWER_ON;
            case 2 -> Position.ALARM_SOS;
            case 5 -> Position.ALARM_POWER_OFF;
            case 7 -> Position.ALARM_GEOFENCE_ENTER;
            case 8 -> Position.ALARM_GEOFENCE_EXIT;
            case 22 -> Position.ALARM_LOW_BATTERY;
            case 25 -> Position.ALARM_MOVEMENT;
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

        int event = parser.nextInt();
        position.set(Position.KEY_EVENT, event);
        position.set(Position.KEY_ALARM, decodeAlarm(event));

        if (event == 11) {
            position.set(Position.KEY_IGNITION, true);
        } else if (event == 12) {
            position.set(Position.KEY_IGNITION, false);
        }

        position.setTime(parser.nextDateTime(Parser.DateTimeFormat.DMY_HMS));

        if (parser.hasNext(2)) {
            position.setLongitude(parser.nextDouble() / 1000000);
            position.setLatitude(parser.nextDouble() / 1000000);
        }

        if (parser.hasNext(2)) {
            position.setLongitude(parser.nextDouble() / 10000);
            position.setLatitude(parser.nextDouble() / 10000);
        }

        position.setValid(parser.nextInt() != 1);
        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextDouble()));
        position.setCourse(parser.nextDouble());

        position.set(Position.KEY_SATELLITES, parser.nextInt());
        position.set(Position.KEY_BATTERY, parser.nextInt());

        return position;
    }

}

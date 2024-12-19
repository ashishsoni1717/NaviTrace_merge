
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

public class IntellitracProtocolDecoder extends BaseProtocolDecoder {

    public IntellitracProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .expression(".+,").optional()
            .number("(d+),")                     // identifier
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("(-?d+.d+),")                // longitude
            .number("(-?d+.d+),")                // latitude
            .number("(d+.?d*),")                 // speed
            .number("(d+.?d*),")                 // course
            .number("(-?d+.?d*),")               // altitude
            .number("(d+),")                     // satellites
            .number("(d+),")                     // event
            .number("(d+),")                     // input
            .number("(d+),?")                    // output
            .number("(d+.d+)?,?")                // adc1
            .number("(d+.d+)?,?")                // adc2
            .groupBegin()
            .number("d{14},d+,")
            .number("(d+),")                     // vss
            .number("(d+),")                     // rpm
            .number("(-?d+),")                   // coolant
            .number("(d+),")                     // fuel
            .number("(d+),")                     // fuel consumption
            .number("(-?d+),")                   // fuel temperature
            .number("(d+),")                     // charger pressure
            .number("(d+),")                     // tpl
            .number("(d+),")                     // axle weight
            .number("(d+)")                      // odometer
            .groupEnd("?")
            .any()
            .compile();

    private String decodeAlarm(int value) {
        return switch (value) {
            case 164 -> Position.ALARM_GEOFENCE_ENTER;
            case 165 -> Position.ALARM_GEOFENCE_EXIT;
            case 168, 169 -> Position.ALARM_LOW_POWER;
            case 170 -> Position.ALARM_POWER_OFF;
            case 176 -> Position.ALARM_POWER_RESTORED;
            case 180 -> Position.ALARM_FALL_DOWN;
            case 225 -> Position.ALARM_JAMMING;
            case 995 -> Position.ALARM_SOS;
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

        position.setTime(parser.nextDateTime());

        position.setValid(true);
        position.setLongitude(parser.nextDouble());
        position.setLatitude(parser.nextDouble());
        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextDouble()));
        position.setCourse(parser.nextDouble());
        position.setAltitude(parser.nextDouble());

        position.set(Position.KEY_SATELLITES, parser.nextInt());

        int event = parser.nextInt();
        position.set(Position.KEY_ALARM, decodeAlarm(event));
        position.set(Position.KEY_EVENT, event);

        position.set(Position.KEY_INPUT, parser.nextInt());
        position.set(Position.KEY_OUTPUT, parser.nextInt());

        position.set(Position.PREFIX_ADC + 1, parser.nextDouble());
        position.set(Position.PREFIX_ADC + 2, parser.nextDouble());

        // J1939 data
        if (parser.hasNext(10)) {
            position.set(Position.KEY_OBD_SPEED, parser.nextInt());
            position.set(Position.KEY_RPM, parser.nextInt());
            position.set("coolant", parser.nextInt());
            position.set(Position.KEY_FUEL_LEVEL, parser.nextInt());
            position.set(Position.KEY_FUEL_CONSUMPTION, parser.nextInt());
            position.set(Position.PREFIX_TEMP + 1, parser.nextInt());
            position.set("chargerPressure", parser.nextInt());
            position.set("tpl", parser.nextInt());
            position.set(Position.KEY_AXLE_WEIGHT, parser.nextInt());
            position.set(Position.KEY_OBD_ODOMETER, parser.nextInt());
        }

        return position;
    }

}

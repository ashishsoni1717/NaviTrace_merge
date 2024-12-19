
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.session.DeviceSession;
import org.navitrace.NetworkMessage;
import org.navitrace.Protocol;
import org.navitrace.helper.ObdDecoder;
import org.navitrace.helper.Parser;
import org.navitrace.helper.PatternBuilder;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.model.Position;

import java.net.SocketAddress;
import java.util.regex.Pattern;

public class ArmoliProtocolDecoder extends BaseProtocolDecoder {

    public ArmoliProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("[M")                          // start
            .number("(d{15})")                   // imei
            .number("(dd)(dd)(dd)")              // date (ddmmyy)
            .number("(dd)(dd)(dd)")              // time (hhmmss)
            .number("([NS])(dd.d{6})")           // latitude
            .number("([EW])(ddd.d{6})")          // longitude
            .number("(d)")                       // valid
            .number("(x)")                       // satellites
            .number("(xx)")                      // speed
            .number("(ddd)")                     // course
            .number("(xxx)")                     // adc 1
            .number("(xxx)")                     // adc 2
            .number("(xx)")                      // status
            .number("(xx)")                      // max speed
            .number("(x{6})")                    // distance
            .number("(dd)?")                     // hdop
            .number("x{4}")                      // idle
            .number(":(x+)").optional()          // alarms
            .number("G(x{6})").optional()        // g-sensor
            .number("H(x{3})").optional()        // power
            .number("E(x{3})").optional()        // battery
            .number("!(x+)").optional()          // driver
            .expression("@A([>0-9A-F]+)").optional() // obd
            .any()
            .compile();

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;
        char type = sentence.charAt(1);

        Position position = new Position(getProtocolName());
        DeviceSession deviceSession;

        if (type != 'M') {
            if (type == 'W') {
                deviceSession = getDeviceSession(channel, remoteAddress);
                if (deviceSession != null) {
                    position.setDeviceId(deviceSession.getDeviceId());
                    getLastLocation(position, null);
                    position.set(
                            Position.KEY_RESULT,
                            sentence.substring(sentence.indexOf(',') + 1, sentence.length() - 1));
                    return position;
                }
            } else if (channel != null && (type == 'Q' || type == 'L')) {
                channel.writeAndFlush(new NetworkMessage("[TX,];;", remoteAddress));
            }
            return null;
        }

        Parser parser = new Parser(PATTERN, (String) msg);
        if (!parser.matches()) {
            return null;
        }

        deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }

        position.setDeviceId(deviceSession.getDeviceId());

        position.setTime(parser.nextDateTime(Parser.DateTimeFormat.DMY_HMS));
        position.setLatitude(parser.nextCoordinate(Parser.CoordinateFormat.HEM_DEG));
        position.setLongitude(parser.nextCoordinate(Parser.CoordinateFormat.HEM_DEG));
        position.setValid(parser.nextInt() > 0);

        position.set(Position.KEY_SATELLITES, parser.nextHexInt());

        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextHexInt()));
        position.setCourse(parser.nextInt());

        position.set(Position.PREFIX_ADC + 1, parser.nextHexInt() / 27.0 * 1000);
        position.set(Position.PREFIX_ADC + 1, parser.nextHexInt() / 27.0 * 1000);
        position.set(Position.KEY_STATUS, parser.nextHexInt());
        position.set("maxSpeed", parser.nextHexInt());
        position.set(Position.KEY_ODOMETER, parser.nextHexInt());

        if (parser.hasNext()) {
            position.set(Position.KEY_HDOP, parser.nextInt() * 0.1);
        }
        if (parser.hasNext()) {
            position.set("alarms", parser.next());
        }
        if (parser.hasNext()) {
            position.set(Position.KEY_G_SENSOR, parser.next());
        }
        if (parser.hasNext()) {
            position.set(Position.KEY_POWER, parser.nextHexInt() * 0.01);
        }
        if (parser.hasNext()) {
            position.set(Position.KEY_BATTERY, parser.nextHexInt() * 0.01);
        }
        if (parser.hasNext()) {
            position.set(Position.KEY_DRIVER_UNIQUE_ID, parser.next());
        }
        if (parser.hasNext()) {
            String[] values = parser.next().split(">");
            for (int i = 1; i < values.length; i++) {
                String value = values[i];
                position.add(ObdDecoder.decodeData(
                        Integer.parseInt(value.substring(4, 6), 16),
                        Long.parseLong(value.substring(6), 16), true));
            }
        }

        return position;
    }

}

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

public class EnvotechProtocolDecoder extends BaseProtocolDecoder {

    public EnvotechProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("$")
            .number("dd")                        // mode
            .expression("...,")                  // hardware
            .number("(x+),")                     // event
            .number("x+,")                       // group
            .number("(x+),")                     // device id
            .number("(dd)(dd)(dd)")              // date (ddmmyy)
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("xx")                        // connection status
            .number("(dd)")                      // rssi
            .number("d{5},")                     // mcc
            .number("(ddd)")                     // power
            .number("(ddd),")                    // battery
            .number("(xx)")                      // inputs
            .number("(xx),")                     // outputs
            .number("(xxx)?")                    // fuel
            .number("(xxx)?,")                   // weight
            .number("(x{8}),")                   // status
            .expression("[^']*'")
            .number("(dd)(dd)(dd)")              // date (ddmmyy)
            .number("(dd)(dd)(dd)")              // time (hhmmss)
            .number("(d)")                       // fix
            .number("(d+)(d{5})([NS])")          // latitude
            .number("(d+)(d{5})([EW])")          // longitude
            .number("(ddd)")                     // speed
            .number("(ddd)")                     // course
            .any()
            .compile();

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        Parser parser = new Parser(PATTERN, (String) msg);
        if (!parser.matches()) {
            return null;
        }

        Position position = new Position(getProtocolName());

        int event = parser.nextHexInt();
        switch (event) {
            case 0x60 -> position.set(Position.KEY_ALARM, Position.ALARM_LOCK);
            case 0x61 -> position.set(Position.KEY_ALARM, Position.ALARM_UNLOCK);
        }
        position.set(Position.KEY_EVENT, event);

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }
        position.setDeviceId(deviceSession.getDeviceId());

        position.setDeviceTime(parser.nextDateTime(Parser.DateTimeFormat.DMY_HMS));

        position.set(Position.KEY_RSSI, parser.nextInt());
        position.set(Position.KEY_POWER, parser.nextInt() * 0.01);
        position.set(Position.KEY_BATTERY, parser.nextInt() * 0.01);
        position.set(Position.KEY_INPUT, parser.nextHexInt());
        position.set(Position.PREFIX_OUT, parser.nextHexInt());
        position.set(Position.KEY_FUEL_LEVEL, parser.nextHexInt());
        position.set("weight", parser.nextHexInt());
        position.set(Position.KEY_STATUS, parser.nextHexLong());

        position.setFixTime(parser.nextDateTime(Parser.DateTimeFormat.DMY_HMS));
        position.setValid(parser.nextInt() > 0);
        position.setLatitude(parser.nextCoordinate(Parser.CoordinateFormat.DEG_DEG_HEM));
        position.setLongitude(parser.nextCoordinate(Parser.CoordinateFormat.DEG_DEG_HEM));
        position.setSpeed(parser.nextInt());
        position.setCourse(parser.nextInt());

        return position;
    }

}

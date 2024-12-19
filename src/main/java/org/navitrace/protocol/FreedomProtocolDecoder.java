
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

public class FreedomProtocolDecoder extends BaseProtocolDecoder {

    public FreedomProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("IMEI,")
            .number("(d+),")                     // imei
            .number("(dddd)/(dd)/(dd), ")        // date (yyyy/dd/mm)
            .number("(dd):(dd):(dd), ")          // time (hh:mm:ss)
            .expression("([NS]), ")
            .number("Lat:(dd)(d+.d+), ")         // latitude
            .expression("([EW]), ")
            .number("Lon:(ddd)(d+.d+), ")        // longitude
            .text("Spd:").number("(d+.d+)")      // speed
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

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }
        position.setDeviceId(deviceSession.getDeviceId());

        position.setValid(true);

        position.setTime(parser.nextDateTime());

        position.setLatitude(parser.nextCoordinate(Parser.CoordinateFormat.HEM_DEG_MIN));
        position.setLongitude(parser.nextCoordinate(Parser.CoordinateFormat.HEM_DEG_MIN));

        position.setSpeed(parser.nextDouble(0));

        return position;
    }

}
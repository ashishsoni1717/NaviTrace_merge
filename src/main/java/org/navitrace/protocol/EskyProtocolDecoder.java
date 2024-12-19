
package org.navitrace.protocol;

import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramChannel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.session.DeviceSession;
import org.navitrace.NetworkMessage;
import org.navitrace.Protocol;
import org.navitrace.helper.BitUtil;
import org.navitrace.helper.Parser;
import org.navitrace.helper.PatternBuilder;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.model.Position;

import java.net.SocketAddress;
import java.util.regex.Pattern;

public class EskyProtocolDecoder extends BaseProtocolDecoder {

    public EskyProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .expression("..;")                   // header
            .number("d+;")
            .number("(d+);")                     // imei
            .text("R;")                          // data type
            .number("(d+)[+;]")                  // satellites
            .number("(dd)(dd)(dd)")              // date
            .number("(dd)(dd)(dd)[+;]")          // time
            .number("(-?d+.d+)[+;]")             // latitude
            .number("(-?d+.d+)[+;]")             // longitude
            .number("(d+.d+)[+;]")               // speed
            .number("(d+)[+;]")                  // course
            .groupBegin()
            .text("0x").number("(x+)[+;]")       // input
            .number("(d+)[+;]")                  // message type
            .number("(d+)[+;]")                  // odometer
            .groupEnd("?")
            .number("(d+)[+;]")                  // adc 1
            .number("(d+)")                      // voltage
            .any()
            .compile();

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;
        Parser parser = new Parser(PATTERN, sentence);
        if (!parser.matches()) {
            return null;
        }

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        position.set(Position.KEY_SATELLITES, parser.nextInt());

        position.setValid(true);
        position.setTime(parser.nextDateTime());
        position.setLatitude(parser.nextDouble());
        position.setLongitude(parser.nextDouble());
        position.setSpeed(UnitsConverter.knotsFromMps(parser.nextDouble()));
        position.setCourse(parser.nextDouble());

        if (parser.hasNext(3)) {
            int input = parser.nextHexInt();
            position.set(Position.KEY_IGNITION, !BitUtil.check(input, 0));
            position.set(Position.PREFIX_IN + 1, !BitUtil.check(input, 1));
            position.set(Position.PREFIX_IN + 2, !BitUtil.check(input, 2));
            position.set(Position.KEY_EVENT, parser.nextInt());
            position.set(Position.KEY_ODOMETER, parser.nextInt());
        }

        position.set(Position.PREFIX_ADC + 1, parser.nextInt());
        position.set(Position.KEY_BATTERY, parser.nextInt() * 0.01);

        int index = sentence.lastIndexOf('+');
        if (index > 0 && channel instanceof DatagramChannel) {
            channel.writeAndFlush(new NetworkMessage("ACK," + sentence.substring(index + 1) + "#", remoteAddress));
        }

        return position;
    }

}

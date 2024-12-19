
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.session.DeviceSession;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.model.Position;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;

public class StarcomProtocolDecoder extends BaseProtocolDecoder {

    public StarcomProtocolDecoder(StarcomProtocol protocol) {
        super(protocol);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;
        sentence = sentence.substring(sentence.indexOf('|') + 1, sentence.lastIndexOf('|'));

        Position position = new Position();
        position.setProtocol(getProtocolName());

        for (String entry : sentence.split(",")) {
            int delimiter = entry.indexOf('=');
            String key = entry.substring(0, delimiter);
            String value = entry.substring(delimiter + 1);
            switch (key) {
                case "unit" -> {
                    DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, value);
                    if (deviceSession != null) {
                        position.setDeviceId(deviceSession.getDeviceId());
                    }
                }
                case "gps_valid" -> position.setValid(Integer.parseInt(value) != 0);
                case "datetime_actual" -> position.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(value));
                case "latitude" -> position.setLatitude(Double.parseDouble(value));
                case "longitude" -> position.setLongitude(Double.parseDouble(value));
                case "altitude" -> position.setAltitude(Double.parseDouble(value));
                case "velocity" -> position.setSpeed(UnitsConverter.knotsFromKph(Integer.parseInt(value)));
                case "heading" -> position.setCourse(Integer.parseInt(value));
                case "eventid" -> position.set(Position.KEY_EVENT, Integer.parseInt(value));
                case "odometer" -> position.set(Position.KEY_ODOMETER, (long) (Double.parseDouble(value) * 1000));
                case "satellites" -> position.set(Position.KEY_SATELLITES, Integer.parseInt(value));
                case "ignition" -> position.set(Position.KEY_IGNITION, Integer.parseInt(value) != 0);
                case "door" -> position.set(Position.KEY_DOOR, Integer.parseInt(value) != 0);
                case "arm" -> position.set(Position.KEY_ARMED, Integer.parseInt(value) != 0);
                case "fuel" -> position.set(Position.KEY_FUEL_LEVEL, Integer.parseInt(value));
                case "rpm" -> position.set(Position.KEY_RPM, Integer.parseInt(value));
                case "main_voltage" -> position.set(Position.KEY_POWER, Double.parseDouble(value));
                case "backup_voltage" -> position.set(Position.KEY_BATTERY, Double.parseDouble(value));
                case "analog1", "analog2", "analog3" -> position.set(
                        Position.PREFIX_ADC + (key.charAt(key.length() - 1) - '0'), Double.parseDouble(value));
                default -> position.set(key, value);
            }
        }

        return position;
    }

}

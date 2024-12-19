
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.session.DeviceSession;
import org.navitrace.NetworkMessage;
import org.navitrace.Protocol;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.model.Position;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.io.StringReader;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TeraTrackProtocolDecoder extends BaseProtocolDecoder {

    public TeraTrackProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        JsonObject json = Json.createReader(new StringReader((String) msg)).readObject();

        String deviceId = json.getString("MDeviceID");
        String imei = json.getString("IMEI");
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, deviceId, imei);
        if (deviceSession == null) {
            return null;
        }

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        position.setTime(dateFormat.parse(json.getString("DateTime")));

        position.setValid(true);
        position.setLatitude(Double.parseDouble(json.getString("Latitude")));
        position.setLongitude(Double.parseDouble(json.getString("Longitude")));
        position.setSpeed(UnitsConverter.knotsFromKph(Integer.parseInt(json.getString("Speed"))));

        position.set(Position.KEY_ODOMETER, Integer.parseInt(json.getString("Mileage")));
        position.set(Position.KEY_LOCK, json.getString("LockOpen").equals("0"));
        position.set(Position.KEY_DRIVER_UNIQUE_ID, json.getString("CardNo"));
        position.set(Position.KEY_ALARM, json.getString("LowPower").equals("1") ? Position.ALARM_LOW_POWER : null);
        position.set(Position.KEY_BATTERY_LEVEL, Integer.parseInt(json.getString("Power")));
        position.set(Position.KEY_RSSI, Integer.parseInt(json.getString("GSM")));

        if (channel != null && json.getString("MessageAck").equals("1")) {
            channel.writeAndFlush(new NetworkMessage("{01}", remoteAddress));
        }

        return position;
    }

}

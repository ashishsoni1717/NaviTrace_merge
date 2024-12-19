
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.session.DeviceSession;
import org.navitrace.Protocol;
import org.navitrace.model.Position;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.io.StringReader;
import java.net.SocketAddress;
import java.time.OffsetDateTime;
import java.util.Date;

public class HoopoProtocolDecoder extends BaseProtocolDecoder {

    public HoopoProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        JsonObject json = Json.createReader(new StringReader((String) msg)).readObject();

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, json.getString("deviceId"));
        if (deviceSession == null) {
            return null;
        }

        if (json.containsKey("eventData")) {

            JsonObject eventData = json.getJsonObject("eventData");

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            Date time = new Date(OffsetDateTime.parse(json.getString("eventTime")).toInstant().toEpochMilli());
            position.setTime(time);

            position.setValid(true);
            position.setLatitude(eventData.getJsonNumber("latitude").doubleValue());
            position.setLongitude(eventData.getJsonNumber("longitude").doubleValue());

            position.set(Position.KEY_EVENT, eventData.getString("eventType"));
            position.set(Position.KEY_BATTERY_LEVEL, eventData.getInt("batteryLevel"));

            if (json.containsKey("movement")) {
                position.setSpeed(json.getJsonObject("movement").getInt("Speed"));
            }

            return position;

        }

        return null;
    }

}

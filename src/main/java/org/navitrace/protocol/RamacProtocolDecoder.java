
package org.navitrace.protocol;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.navitrace.BaseHttpProtocolDecoder;
import org.navitrace.Protocol;
import org.navitrace.model.Position;
import org.navitrace.session.DeviceSession;

import java.io.StringReader;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RamacProtocolDecoder extends BaseHttpProtocolDecoder {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RamacProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        FullHttpRequest request = (FullHttpRequest) msg;
        String content = request.content().toString(StandardCharsets.UTF_8);
        JsonObject json = Json.createReader(new StringReader(content)).readObject();

        String deviceId = json.getString("DeviceId");
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, deviceId);
        if (deviceSession == null) {
            sendResponse(channel, HttpResponseStatus.BAD_REQUEST);
            return null;
        }

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        position.set(Position.KEY_TYPE, json.getInt("PacketType"));
        position.set(Position.KEY_INDEX, json.getInt("SeqNumber"));
        position.setDeviceTime(dateFormat.parse(json.getString("UpdateDate")));

        int alert = json.getInt("Alert");
        if (alert > 0) {
            position.set("alert", alert);
            String alertMessage = json.getString("AlertMessage");
            if (!alertMessage.isEmpty()) {
                position.set("alertMessage", alertMessage);
            }
        }

        if (json.containsKey("GpsEvent")) {
            position.set("gpsEvent", json.getInt("GpsEvent"));
            if (json.containsKey("GpsEventText")) {
                position.set("gpsEventText", json.getString("GpsEventText"));
            }
        }

        if (json.containsKey("Event")) {
            position.set(Position.KEY_EVENT, json.getInt("Event"));
        }
        if (json.containsKey("BatteryPercentage")) {
            position.set(Position.KEY_BATTERY_LEVEL, json.getInt("BatteryPercentage"));
        }
        if (json.containsKey("Battery")) {
            position.set(Position.KEY_BATTERY, json.getJsonNumber("Battery").doubleValue());
        }

        position.set("deviceType", json.getString("DeviceTypeText"));

        if (json.containsKey("Latitude") && json.containsKey("Longitude")) {
            position.setValid(true);
            if (json.containsKey("LocationDateTime")) {
                position.setFixTime(dateFormat.parse(json.getString("LocationDateTime")));
            } else {
                position.setFixTime(position.getDeviceTime());
            }
            position.setLatitude(json.getJsonNumber("Latitude").doubleValue());
            position.setLongitude(json.getJsonNumber("Longitude").doubleValue());
        } else {
            getLastLocation(position, position.getDeviceTime());
        }

        sendResponse(
                channel, HttpResponseStatus.OK,
                Unpooled.copiedBuffer("{\"CaseID\":1,\"EventID\":1}", StandardCharsets.UTF_8));
        return position;
    }

}

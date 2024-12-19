
package org.navitrace.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.navitrace.BaseHttpProtocolDecoder;
import org.navitrace.Protocol;
import org.navitrace.helper.DateUtil;
import org.navitrace.model.Position;
import org.navitrace.session.DeviceSession;

import java.io.StringReader;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class DraginoProtocolDecoder extends BaseHttpProtocolDecoder {

    public DraginoProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        FullHttpRequest request = (FullHttpRequest) msg;
        String content = request.content().toString(StandardCharsets.UTF_8);
        JsonObject json = Json.createReader(new StringReader(content)).readObject();

        String deviceId = json.getJsonObject("end_device_ids").getString("device_id");
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, deviceId);
        if (deviceSession == null) {
            sendResponse(channel, HttpResponseStatus.BAD_REQUEST);
            return null;
        }

        JsonObject message = json.getJsonObject("uplink_message");
        JsonObject decoded = message.getJsonObject("decoded_payload");

        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        position.setTime(DateUtil.parseDate(message.getString("received_at")));

        position.setValid(true);
        position.setLatitude(decoded.getJsonNumber("Latitude").doubleValue());
        position.setLongitude(decoded.getJsonNumber("Longitude").doubleValue());

        position.set("humidity", decoded.getJsonNumber("Hum").doubleValue());
        position.set(Position.KEY_BATTERY, decoded.getJsonNumber("BatV").doubleValue());
        position.set(Position.PREFIX_TEMP + 1, decoded.getJsonNumber("Tem").doubleValue());

        if (Boolean.parseBoolean(decoded.getString("ALARM_status"))) {
            position.set(Position.KEY_ALARM, Position.ALARM_SOS);
        }

        sendResponse(channel, HttpResponseStatus.OK);
        return position;
    }

}

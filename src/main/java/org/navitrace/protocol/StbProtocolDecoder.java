
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.NetworkMessage;
import org.navitrace.Protocol;
import org.navitrace.model.Position;
import org.navitrace.session.DeviceSession;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.io.StringReader;
import java.net.SocketAddress;
import java.util.Date;

public class StbProtocolDecoder extends BaseProtocolDecoder {

    public StbProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    public static final int MSG_LOGIN = 110;
    public static final int MSG_PROPERTY = 310;
    public static final int MSG_ALARM = 410;

    private void sendResponse(
            Channel channel, SocketAddress remoteAddress, int type, String deviceId, JsonObject root) {
        String response = String.format(
                "{ \"msgType\": %d, \"devId\": \"%s\", \"result\": 1, \"txnNo\": \"%s\" }",
                type + 1, deviceId, root.getString("txnNo"));
        if (channel != null) {
            channel.writeAndFlush(new NetworkMessage(response, remoteAddress));
        }
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        JsonObject root = Json.createReader(new StringReader((String) msg)).readObject();
        int type = root.getInt("msgType");
        String deviceId = root.getString("devId");

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, deviceId);
        if (deviceSession == null) {
            return null;
        }

        sendResponse(channel, remoteAddress, type, deviceId, root);

        if (type == MSG_PROPERTY || type == MSG_ALARM) {

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            if (type == MSG_PROPERTY) {
                int locationType = 0;
                for (JsonValue property : root.getJsonArray("attrList")) {
                    JsonObject propertyObject = property.asJsonObject();
                    String id = propertyObject.getString("id");
                    switch (id) {
                        case "01101001" -> locationType = Integer.parseInt(propertyObject.getString("value"));
                        case "01102001" -> position.setLongitude(
                                Double.parseDouble(propertyObject.getString("value")));
                        case "01103001" -> position.setLatitude(
                                Double.parseDouble(propertyObject.getString("value")));
                        case "01118001" -> position.set(
                                Position.KEY_DEVICE_TEMP, Double.parseDouble(propertyObject.getString("value")));
                        case "01122001" -> position.set(
                                "batteryControl", Integer.parseInt(propertyObject.getString("value")));
                        case "02301001" -> position.set(
                                "switchCabinetCommand", Integer.parseInt(propertyObject.getString("value")));
                        default -> {
                            String key = "id" + id;
                            if (propertyObject.containsKey("doorId")) {
                                key += "Door" + propertyObject.getString("doorId");
                            }
                            position.set(key, propertyObject.getString("value"));
                        }
                    }
                }
                if (locationType > 0) {
                    position.setTime(new Date());
                    position.setValid(locationType != 5);
                    if (locationType == 2 || locationType == 4) {
                        position.setLongitude(-position.getLongitude());
                    }
                    if (locationType == 3 || locationType == 4) {
                        position.setLatitude(-position.getLatitude());
                    }
                } else {
                    getLastLocation(position, null);
                }
            }

            return position;
        }

        return null;
    }

}

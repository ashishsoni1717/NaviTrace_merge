
package org.navitrace.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import org.navitrace.BaseHttpProtocolDecoder;
import org.navitrace.Protocol;
import org.navitrace.model.Position;
import org.navitrace.session.DeviceSession;

import java.io.StringReader;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ValtrackProtocolDecoder extends BaseHttpProtocolDecoder {

    public ValtrackProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        FullHttpRequest request = (FullHttpRequest) msg;
        String content = request.content().toString(StandardCharsets.UTF_8);
        JsonObject object = Json.createReader(new StringReader(content)).readObject();
        JsonArray messages = object.getJsonArray("resource");

        List<Position> positions = new LinkedList<>();
        for (int i = 0; i < messages.size(); i++) {

            JsonObject message = messages.getJsonObject(i);
            String id = message.getString("devid");

            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, id);
            if (deviceSession == null) {
                continue;
            }

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            position.setValid(true);
            position.setTime(new Date());
            position.setLatitude(Double.parseDouble(message.getString("lat")));
            position.setLongitude(Double.parseDouble(message.getString("lon")));
            String speed = message.getString("speed");
            if (!speed.isEmpty()) {
                position.setSpeed(Double.parseDouble(speed));
            }

            position.set(Position.KEY_BATTERY, Double.parseDouble(message.getString("vbat")));

            positions.add(position);

        }

        sendResponse(channel, HttpResponseStatus.OK);
        return positions;
    }

}

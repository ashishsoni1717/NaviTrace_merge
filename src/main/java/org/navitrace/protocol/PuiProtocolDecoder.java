
package org.navitrace.protocol;

import io.netty.handler.codec.mqtt.MqttPublishMessage;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.apache.kafka.common.utils.ByteBufferInputStream;
import org.navitrace.BaseMqttProtocolDecoder;
import org.navitrace.Protocol;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.model.Position;
import org.navitrace.session.DeviceSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PuiProtocolDecoder extends BaseMqttProtocolDecoder {

    public PuiProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object decode(DeviceSession deviceSession, MqttPublishMessage message) throws Exception {

        JsonObject json;
        try (ByteBufferInputStream inputStream = new ByteBufferInputStream(message.payload().nioBuffer())) {
            json = Json.createReader(inputStream).readObject();
        }

        String type = json.getString("rpt");
        switch (type) {
            case "hf":
            case "loc":
                Position position = new Position(getProtocolName());
                position.setDeviceId(deviceSession.getDeviceId());

                position.setValid(true);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                position.setTime(dateFormat.parse(json.getString("ts")));

                JsonObject location = json.getJsonObject("location");
                position.setLatitude(location.getJsonNumber("lat").doubleValue());
                position.setLongitude(location.getJsonNumber("lon").doubleValue());

                position.setCourse(json.getInt("bear"));
                position.setSpeed(UnitsConverter.knotsFromCps(json.getInt("spd")));

                position.set(Position.KEY_IGNITION, json.getString("ign").equals("on"));

                return position;

            default:
                return null;
        }
    }

}

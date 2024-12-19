
package org.navitrace.forward;

import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PositionForwarderMqtt implements PositionForwarder {

    private final MqttClient mqttClient;
    private final ObjectMapper objectMapper;

    private final String topic;

    public PositionForwarderMqtt(final Config config, final ObjectMapper objectMapper) {
        this.topic = config.getString(Keys.FORWARD_TOPIC);
        mqttClient = new MqttClient(config.getString(Keys.FORWARD_URL));
        this.objectMapper = objectMapper;
    }

    @Override
    public void forward(PositionData positionData, ResultHandler resultHandler) {
        try {
            String payload = objectMapper.writeValueAsString(positionData);
            mqttClient.publish(topic, payload, (message, e) -> resultHandler.onResult(e == null, e));
        } catch (JsonProcessingException e) {
            resultHandler.onResult(false, e);
        }
    }

}

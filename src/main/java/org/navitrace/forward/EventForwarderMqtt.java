
package org.navitrace.forward;

import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventForwarderMqtt implements EventForwarder {

    private final MqttClient mqttClient;
    private final ObjectMapper objectMapper;

    private final String topic;

    public EventForwarderMqtt(Config config, ObjectMapper objectMapper) {
        this.topic = config.getString(Keys.EVENT_FORWARD_TOPIC);
        mqttClient = new MqttClient(config.getString(Keys.EVENT_FORWARD_URL));
        this.objectMapper = objectMapper;
    }

    @Override
    public void forward(EventData eventData, ResultHandler resultHandler) {
        try {
            String payload = objectMapper.writeValueAsString(eventData);
            mqttClient.publish(topic, payload, (message, e) -> resultHandler.onResult(e == null, e));
        } catch (JsonProcessingException e) {
            resultHandler.onResult(false, e);
        }
    }

}

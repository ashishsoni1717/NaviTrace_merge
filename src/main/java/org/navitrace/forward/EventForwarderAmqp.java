
package org.navitrace.forward;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import java.io.IOException;

public class EventForwarderAmqp implements EventForwarder {

    private final AmqpClient amqpClient;
    private final ObjectMapper objectMapper;

    public EventForwarderAmqp(Config config, ObjectMapper objectMapper) {
        String connectionUrl = config.getString(Keys.EVENT_FORWARD_URL);
        String exchange = config.getString(Keys.EVENT_FORWARD_EXCHANGE);
        String topic = config.getString(Keys.EVENT_FORWARD_TOPIC);
        this.objectMapper = objectMapper;
        amqpClient = new AmqpClient(connectionUrl, exchange, topic);
    }

    @Override
    public void forward(EventData eventData, ResultHandler resultHandler) {
        try {
            String value = objectMapper.writeValueAsString(eventData);
            amqpClient.publishMessage(value);
            resultHandler.onResult(true, null);
        } catch (IOException e) {
            resultHandler.onResult(false, e);
        }
    }
}

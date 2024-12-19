
package org.navitrace.forward;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import java.io.IOException;

public class PositionForwarderAmqp implements PositionForwarder {

    private final AmqpClient amqpClient;
    private final ObjectMapper objectMapper;

    public PositionForwarderAmqp(Config config, ObjectMapper objectMapper) {
        String connectionUrl = config.getString(Keys.FORWARD_URL);
        String exchange = config.getString(Keys.FORWARD_EXCHANGE);
        String topic = config.getString(Keys.FORWARD_TOPIC);
        amqpClient = new AmqpClient(connectionUrl, exchange, topic);
        this.objectMapper = objectMapper;
    }

    @Override
    public void forward(PositionData positionData, ResultHandler resultHandler) {
        try {
            String value = objectMapper.writeValueAsString(positionData);
            amqpClient.publishMessage(value);
            resultHandler.onResult(true, null);
        } catch (IOException e) {
            resultHandler.onResult(false, e);
        }
    }
}

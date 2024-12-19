
package org.navitrace.forward;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class AmqpClient {
    private final Channel channel;
    private final String exchange;
    private final String topic;

    AmqpClient(String connectionUrl, String exchange, String topic) {
        this.exchange = exchange;
        this.topic = topic;

        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUri(connectionUrl);
        } catch (NoSuchAlgorithmException | URISyntaxException | KeyManagementException e) {
            throw new RuntimeException("Error while setting URI for RabbitMQ connection factory", e);
        }

        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC, true);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException("Error while creating and configuring RabbitMQ channel", e);
        }
    }

    public void publishMessage(String message) throws IOException {
        channel.basicPublish(exchange, topic, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
    }
}

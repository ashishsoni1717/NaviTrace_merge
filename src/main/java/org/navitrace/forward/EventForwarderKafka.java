
package org.navitrace.forward;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import java.util.Properties;

public class EventForwarderKafka implements EventForwarder {

    private final Producer<String, String> producer;
    private final ObjectMapper objectMapper;

    private final String topic;

    public EventForwarderKafka(Config config, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        Properties properties = new Properties();
        properties.put("bootstrap.servers", config.getString(Keys.EVENT_FORWARD_URL));
        properties.put("acks", "all");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(properties);
        topic = config.getString(Keys.EVENT_FORWARD_TOPIC);
    }

    @Override
    public void forward(EventData eventData, ResultHandler resultHandler) {
        try {
            String key = Long.toString(eventData.getDevice().getId());
            String value = objectMapper.writeValueAsString(eventData);
            producer.send(new ProducerRecord<>(topic, key, value));
            resultHandler.onResult(true, null);
        } catch (JsonProcessingException e) {
            resultHandler.onResult(false, e);
        }
    }

}

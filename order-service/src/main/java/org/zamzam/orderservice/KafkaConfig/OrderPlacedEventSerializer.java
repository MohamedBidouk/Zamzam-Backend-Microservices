package org.zamzam.orderservice.KafkaConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.zamzam.orderservice.event.OrderPlacedEvent;

import java.util.Map;

public class OrderPlacedEventSerializer implements Serializer<OrderPlacedEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void configure(Map<String, ?> configs, boolean isKey){
    }
    @Override
    public byte[] serialize(String s, OrderPlacedEvent orderPlacedEvent) {
        try {
            return objectMapper.writeValueAsBytes(orderPlacedEvent);
        } catch (Exception e) {
            throw new SerializationException("Error serializing message", e);
        }
    }
    @Override
    public void close(){
    }
}

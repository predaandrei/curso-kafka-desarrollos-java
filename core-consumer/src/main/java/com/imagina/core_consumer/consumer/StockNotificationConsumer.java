package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_consumer.model.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockNotificationConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-stocks", groupId = "consumer-group-notification")
    public void consumer(String message) {
        try {
            var stock = objectMapper.readValue(message, Stock.class);
            log.info("Procesado de la Notificacion para : {}", stock.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

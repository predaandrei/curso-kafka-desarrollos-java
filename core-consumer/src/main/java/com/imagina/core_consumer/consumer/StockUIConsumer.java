package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_consumer.model.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

@Service
@Slf4j
public class StockUIConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-stocks", groupId = "consumer-group-ui")
    public void consumer(String message) {
        try {
            var stock = objectMapper.readValue(message, Stock.class);
            var randomDelayMillis = ThreadLocalRandom.current().nextLong(500, 2000);
            MILLISECONDS.sleep(randomDelayMillis);
            log.info("Procesado de la UI para : {}", stock.toString());
        } catch (JsonProcessingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

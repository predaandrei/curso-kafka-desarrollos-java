package com.imagina.core_consumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class HolaMundoKafkaConsumer {

    @KafkaListener(topics = "t-hola-mundo")
    public void consume(String message) {
        log.info(message);
    }
}

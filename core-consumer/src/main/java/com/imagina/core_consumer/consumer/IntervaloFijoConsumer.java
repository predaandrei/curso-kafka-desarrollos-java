package com.imagina.core_consumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class IntervaloFijoConsumer {

    //@KafkaListener(topics = "t-intervalo-fijo")
    public void consume(String message) {
        log.info("Consumiendo el mensaje: {}", message);
    }
}

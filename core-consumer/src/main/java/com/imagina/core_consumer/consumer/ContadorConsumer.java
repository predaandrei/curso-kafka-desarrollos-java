package com.imagina.core_consumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ContadorConsumer {

    @KafkaListener(topics = "t-contador", groupId = "contador-group-fast", containerFactory = "recordAckFactory")
    public void readFast(String message) {
        log.info("Fast: " + message);
    }

    @KafkaListener(topics = "t-contador", groupId = "contador-group-slow", containerFactory = "batchAckFactory")
    public void readSlow(String message) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        log.info("Slow: " + message);
    }
}
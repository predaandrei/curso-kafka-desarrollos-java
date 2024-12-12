package com.imagina.core_consumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

//@Service
@Slf4j
public class KafkaKeyConsumer {

    @KafkaListener(topics = "t-multi-particiones", concurrency = "3")
    public void consume(ConsumerRecord<String, String> record) throws InterruptedException {
        log.info("Key: {}, Partition: {}, Mensaje: {}", record.key(), record.partition(), record.value());
        TimeUnit.SECONDS.sleep(1);
    }
}

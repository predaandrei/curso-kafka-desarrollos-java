package com.imagina.core_producer.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ContadorProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(int number) {
        for (int i = 0; i < number; i++) {
            String message = "Data " + i;
            kafkaTemplate.send("t-contador", message);
        }
    }
}
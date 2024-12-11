package com.imagina.core_producer.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Service
public class HolaMundoKafkaProducer {

    //@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendHolaMundo(String nombre) {
        kafkaTemplate.send("t-hola-mundo", "Hola " + nombre);
    }

}

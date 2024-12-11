package com.imagina.core_producer.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//@Service
//@Slf4j
public class IntervaloFijoProducer {

    //@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private int i = 0;

    //@Scheduled(fixedRate = 1000)
    public void sendMessage() {
        i++;
        //log.info("i es {}", i);
        kafkaTemplate.send("t-intervalo-fijo", "Mensaje de intervalo fijo " + i);
    }
}

package com.imagina.core_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.NumeroSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Service
public class NumeroSimpleProducer {

    //@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //@Autowired
    private ObjectMapper objectMapper;

    public void send(NumeroSimple numeroSimple) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(numeroSimple);
        kafkaTemplate.send("t-numero-simple", json);
    }

}
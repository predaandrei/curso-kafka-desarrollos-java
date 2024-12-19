package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_consumer.model.NumeroSimple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class NumeroSimpleConsumer {

    //@Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-numero-simple")
    public void consume(String message) throws JsonMappingException, JsonProcessingException {
        var numeroSimple = objectMapper.readValue(message, NumeroSimple.class);

        if (numeroSimple.getNumero() % 2 != 0) {
            throw new IllegalArgumentException("Numero impar : " + numeroSimple.getNumero());
        }

        log.info("Procesando el numero simple : {}", numeroSimple);
    }
}

package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_consumer.model.PlanePosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlanePositionConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-posicion", groupId = "consumer-group-all-positions")
    public void listenAll(String message) throws JsonProcessingException {
        var planePosition = objectMapper.readValue(message, PlanePosition.class);
        log.info("Escuchando todas las posiciones: {}", planePosition);
    }

    @KafkaListener(topics = "t-posicion", groupId = "consumer-group-near-positions", containerFactory = "nearPositionContainerFactory")
    public void listenNear(String message) throws JsonProcessingException {
        var planePosition = objectMapper.readValue(message, PlanePosition.class);
        log.info("Escuchando las posiciones cercanas: {}", planePosition);
    }
}

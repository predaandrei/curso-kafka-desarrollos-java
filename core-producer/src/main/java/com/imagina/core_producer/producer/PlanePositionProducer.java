package com.imagina.core_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.PlanePosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class PlanePositionProducer {

    //@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //@Autowired
    private ObjectMapper objectMapper;


    public void sendPosition(PlanePosition planePosition) throws JsonProcessingException {
        var json =  objectMapper.writeValueAsString(planePosition);
        kafkaTemplate.send("t-posicion", json);
    }
}

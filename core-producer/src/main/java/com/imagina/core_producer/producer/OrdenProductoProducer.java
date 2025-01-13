package com.imagina.core_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.OrdenProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrdenProductoProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void send(OrdenProducto ordenProducto) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(ordenProducto);

        kafkaTemplate.send("t-orden-producto", json);
    }
}

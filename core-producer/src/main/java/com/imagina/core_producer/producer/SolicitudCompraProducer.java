package com.imagina.core_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.SolicitudCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Service
public class SolicitudCompraProducer {

    //@Autowired
    private ObjectMapper objectMapper;

    //@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(SolicitudCompra solicitudCompra) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(solicitudCompra);
        kafkaTemplate.send("t-solicitud-compra", solicitudCompra.getScNumber(), json);
    }
}

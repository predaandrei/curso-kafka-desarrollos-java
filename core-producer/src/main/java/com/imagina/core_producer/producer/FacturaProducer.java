package com.imagina.core_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Service
public class FacturaProducer {

    //@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //@Autowired
    private ObjectMapper objectMapper;

    public void sendFactura(Factura factura) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(factura);
        kafkaTemplate.send("t-factura", (int) factura.getCantidad() % 2, factura.getNumeroFactura(), json);
    }
}
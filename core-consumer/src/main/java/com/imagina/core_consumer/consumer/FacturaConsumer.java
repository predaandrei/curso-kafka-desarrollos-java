package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_consumer.model.Factura;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class FacturaConsumer {

    //@Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-factura", concurrency = "2", containerFactory = "facturaDltContainerFactory")
    public void consume(String message) throws JsonProcessingException {
        var factura = objectMapper.readValue(message, Factura.class);

        if (factura.getCantidad() < 1) {
            throw new IllegalArgumentException("Cantidad incorrecta para la factura " + factura);
        }

        log.info("Procesando la factura : {}", factura);
    }

}
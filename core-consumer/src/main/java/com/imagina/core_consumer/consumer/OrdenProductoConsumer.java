package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_consumer.model.OrdenProducto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrdenProductoConsumer {

    private static final int MAX_CANTIDAD_PRODUCTO = 7;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-orden-producto", errorHandler = "ordenProductoErrorHandler")
    public void consume(String message) throws JsonProcessingException {
        var ordenProducto = objectMapper.readValue(message, OrdenProducto.class);
        if (ordenProducto.getCantidad() > MAX_CANTIDAD_PRODUCTO) {
            throw new IllegalArgumentException("Demasiados productos : " + ordenProducto.getCantidad());
        }
        log.info("Procesando una orden de producto : {}", ordenProducto);
    }
}

package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_consumer.model.Empleado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class EmpleadoConsumer {

    //@Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-empleado")
    public void listenEmpleado(String message) throws JsonProcessingException {
        var empleado = objectMapper.readValue(message, Empleado.class);
        log.info("El empleado es : {}", empleado.toString());
    }
}

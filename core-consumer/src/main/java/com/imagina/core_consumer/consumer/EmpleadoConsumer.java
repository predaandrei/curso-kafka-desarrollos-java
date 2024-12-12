package com.imagina.core_consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_consumer.model.Empleado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
@Service
@Slf4j
public class EmpleadoConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-empleados")
    public void listenEmpleado(String mensaje) throws JsonProcessingException {
        var empleado = objectMapper.readValue(mensaje, Empleado.class);
        log.info("Empleado es {} ", empleado.toString());
=======
//@Service
@Slf4j
public class EmpleadoConsumer {

    //@Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "t-empleado")
    public void listenEmpleado(String message) throws JsonProcessingException {
        var empleado = objectMapper.readValue(message, Empleado.class);
        log.info("El empleado es : {}", empleado.toString());
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
    }
}

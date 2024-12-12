package com.imagina.core_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.Empleado;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmpleadoProducer {

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String,String> kafkaTemplate;

    public void sendEmpleado(Empleado empleado){
        String json = String.valueOf(empleado);
        kafkaTemplate.send("t-empleados",json);
    }
   
}

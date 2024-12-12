package com.imagina.core_producer.producer;

<<<<<<< HEAD
=======
import com.fasterxml.jackson.core.JsonProcessingException;
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.Empleado;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
@Service
=======
//@Service
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
@RequiredArgsConstructor
public class EmpleadoProducer {

    private final ObjectMapper objectMapper;

<<<<<<< HEAD
    private final KafkaTemplate<String,String> kafkaTemplate;

    public void sendEmpleado(Empleado empleado){
        String json = String.valueOf(empleado);
        kafkaTemplate.send("t-empleados",json);
    }
=======
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEmpleado(Empleado empleado) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(empleado);
        kafkaTemplate.send("t-empleado", json);
    }

>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
}

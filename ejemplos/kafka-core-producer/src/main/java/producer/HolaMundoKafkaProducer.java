package producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class HolaMundoKafkaProducer {
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public HolaMundoKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendHolaMundo(String mensaje){
        System.out.println("Enviando mensaje a Kafka: " + mensaje);
        this.kafkaTemplate.send("t-hola-mundo", "Hola " + mensaje);
    }
}

package com.imagina.core_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoProducer {

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String,String> kafkaTemplate;

    public void sendPedido(Pedido pedido) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(pedido);
        kafkaTemplate.send("t-pedidos", json);
    }
}

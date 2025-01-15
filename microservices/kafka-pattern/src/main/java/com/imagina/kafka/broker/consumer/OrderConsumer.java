package com.imagina.kafka.broker.consumer;

import com.imagina.kafka.broker.message.OrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    @KafkaListener(topics = "t-commodity-order")
    public void consume(OrderMessage orderMessage) {
        var totalItemAmount = orderMessage.getPrice() * orderMessage.getQuantity();

        log.info("Procesando orden {}, item {}, total amount {}", orderMessage.getOrderNumber(), orderMessage.getItemName(), totalItemAmount);
    }
}

package com.imagina.kafka.broker.consumer;

import com.imagina.kafka.broker.message.OrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final static Logger LOG = LoggerFactory.getLogger(OrderProducer.class);

    @Autowired
    private KafkaTemplate<String, OrderMessage> kafkaTemplate;

    public void sendOrder(OrderMessage orderMessage) {
        kafkaTemplate.send("t-commodity-order", orderMessage).whenComplete(
                (recordMetadata, ex) -> {
                    if (ex == null) {
                        LOG.info("Order {} sent successfully", orderMessage.getOrderNumber());
                    } else {
                        LOG.error("Failed to send order {}", orderMessage.getOrderNumber(), ex);
                    }
                }
        );
        LOG.info("Un mensaje tonto para la orden {}, item {}", orderMessage.getOrderNumber(), orderMessage.getItemName());
    }
}

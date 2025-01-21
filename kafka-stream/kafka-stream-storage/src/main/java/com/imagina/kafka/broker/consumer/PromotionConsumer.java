package com.imagina.kafka.broker.consumer;

import com.imagina.kafka.broker.message.DiscountMessage;
import com.imagina.kafka.broker.message.PromotionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@KafkaListener(topics = "t-commodity-promotion")
public class PromotionConsumer {

    @KafkaHandler
    public void listenPromotion(PromotionMessage message) {
        log.info("Processing promotion: {}", message);
    }

    @KafkaHandler
    public void listenDiscounts(DiscountMessage message) {
        log.info("Processing discount: {}", message);
    }
}

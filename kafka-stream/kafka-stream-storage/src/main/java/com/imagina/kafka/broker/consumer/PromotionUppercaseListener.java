package com.imagina.kafka.broker.consumer;

import com.imagina.kafka.broker.message.PromotionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PromotionUppercaseListener {

    @KafkaListener(topics = "t-commodity-promotion-uppercase")
    public void listenPromotion(PromotionMessage message) {
        log.info("Processing uppercase promotion: {}", message);
    }

}

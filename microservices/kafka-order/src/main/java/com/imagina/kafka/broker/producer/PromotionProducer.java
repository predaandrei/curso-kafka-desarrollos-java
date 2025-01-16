package com.imagina.kafka.broker.producer;


import com.imagina.kafka.broker.message.PromotionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PromotionProducer {

    @Autowired
    private KafkaTemplate<String, PromotionMessage> kafkaTemplate;

    public void sendPromotion(PromotionMessage message) {
        try {
            var sendResult = kafkaTemplate.send("t-commodity-promotion", message.getPromotionCode(), message)
                    .get(3, TimeUnit.SECONDS);
            log.info("Promotion code: {} sent successfully", sendResult.getProducerRecord().value());
        } catch (Exception e) {
            log.error("Error sending promotion {}", message.getPromotionCode(), e);
        }
        log.info("Un mensaje tonto para la promotion {}", message.getPromotionCode());
    }
}

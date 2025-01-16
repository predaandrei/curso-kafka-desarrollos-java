package com.imagina.kafka.api.command.action;

import com.imagina.kafka.api.request.PromotionRequest;
import com.imagina.kafka.broker.message.PromotionMessage;
import com.imagina.kafka.broker.producer.PromotionProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PromotionAction {

    @Autowired
    private PromotionProducer promotionProducer;



    public PromotionMessage convertToPromotionMessage(PromotionRequest promotionRequest) {
        return new PromotionMessage(promotionRequest.getPromotionCode());
    }

    public void sendToKafka(PromotionMessage promotionMessage) {
        promotionProducer.sendPromotion(promotionMessage);
    }

}

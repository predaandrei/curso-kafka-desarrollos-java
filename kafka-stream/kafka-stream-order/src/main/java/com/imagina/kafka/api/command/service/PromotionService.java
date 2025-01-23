package com.imagina.kafka.api.command.service;

import com.imagina.kafka.api.command.action.PromotionAction;
import com.imagina.kafka.api.request.PromotionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionService {

    @Autowired
    private PromotionAction promotionAction;

    public void createPromotion(PromotionRequest promotionRequest) {
        var message = promotionAction.convertToPromotionMessage(promotionRequest);
        promotionAction.sendToKafka(message);
    }
}

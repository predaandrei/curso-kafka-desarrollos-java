package com.imagina.kafka.api.server;

import com.imagina.kafka.api.command.service.PromotionService;
import com.imagina.kafka.api.request.PromotionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromotionApi {

    @Autowired
    private PromotionService promotionService;

    @PostMapping(value = "/api/promotion", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createPromotion(@RequestBody PromotionRequest request) {
        promotionService.createPromotion(request);
        return new ResponseEntity<>(request.getPromotionCode(), HttpStatus.CREATED);
    }
}

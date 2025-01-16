package com.imagina.kafka.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionRequest {

    private String promotionCode;

    @Override
    public String toString() {
        return "PromotionRequest{" +
                "promotionCode='" + promotionCode + '\'' +
                '}';
    }

}

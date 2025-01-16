package com.imagina.kafka.broker.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountMessage {

    private String discountCode;

    private int discountPercentage;

    @Override
    public String toString() {
        return "DiscountMessage [discountCode=" + discountCode + ", discountPercentage=" + discountPercentage + "]";
    }

}
package com.imagina.kafka.broker.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaRelampagoMessage {

    private String customerId;

    private String itemName;

    @Override
    public String toString() {
        return "FlashSaleVoteMessage [customerId=" + customerId + ", itemName=" + itemName + "]";
    }


}
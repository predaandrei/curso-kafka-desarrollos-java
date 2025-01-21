package com.imagina.kafka.broker.message;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRewardMessage {

    private String orderLocation;

    private String orderNumber;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime orderDateTime;

    private String itemName;

    private int price;

    private int quantity;

    // toString method
    @Override
    public String toString() {
        return "OrderRewardMessage{" +
                "orderLocation='" + orderLocation + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderDateTime=" + orderDateTime +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

}

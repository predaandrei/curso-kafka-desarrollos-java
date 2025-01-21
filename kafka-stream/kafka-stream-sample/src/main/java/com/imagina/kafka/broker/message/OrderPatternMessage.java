package com.imagina.kafka.broker.message;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPatternMessage {

    private String itemName;

    private long totalItemAmount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime orderDateTime;

    private String orderLocation;

    private String orderNumber;

    @Override
    public String toString() {
        return "OrderPatternMessage{" +
                "itemName='" + itemName + '\'' +
                ", totalItemAmount=" + totalItemAmount +
                ", orderDateTime=" + orderDateTime +
                ", orderLocation='" + orderLocation + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }

}

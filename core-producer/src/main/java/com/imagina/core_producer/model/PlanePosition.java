package com.imagina.core_producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanePosition {

    private String plainId;
    private long timestamp;
    private int distancia;
}

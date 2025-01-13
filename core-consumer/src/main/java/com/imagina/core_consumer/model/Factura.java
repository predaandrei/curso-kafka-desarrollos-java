package com.imagina.core_consumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    private String numeroFactura;
    private double cantidad;
    private String divisa;

}
package com.imagina.core_consumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudCompra {

    private UUID solicitudId;
    private String scNumber;
    private int cantidad;
    private String divisa;
}

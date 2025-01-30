package com.imagina.core_consumer.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Empleado {
    private String empleadoId;
    private String nombre;
}

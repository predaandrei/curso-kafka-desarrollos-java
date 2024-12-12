package com.imagina.core_producer.model;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Empleado {
    private String empleadoId;
    private String nombre;
}

package com.imagina.core_producer.model;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Empleado {
    private String empleadoId;
    private String nombre;
    //private LocalDate fechaNacimiento;
}

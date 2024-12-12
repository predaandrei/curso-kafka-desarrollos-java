package com.imagina.core_producer.model;

import lombok.*;

import java.time.LocalDate;

<<<<<<< HEAD
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Empleado {
    private String empleadoId;
    private String nombre;
=======
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Empleado {
    private String empleadoId;
    private String nombre;
    //private LocalDate fechaNacimiento;
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
}

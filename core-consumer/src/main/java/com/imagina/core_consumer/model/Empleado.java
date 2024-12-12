package com.imagina.core_consumer.model;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Empleado {
    private String empleadoId;
    private String nombre;
=======
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Empleado {
    private String empleadoId;
    private String nombre;
    //private LocalDate fechaNacimiento;
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
}

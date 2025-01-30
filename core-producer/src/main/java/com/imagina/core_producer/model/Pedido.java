package com.imagina.core_producer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    public String pedidoId;
    public String cliente;
    public List<String> productos;
    public double total;
    public LocalDateTime fechaPedido;
}

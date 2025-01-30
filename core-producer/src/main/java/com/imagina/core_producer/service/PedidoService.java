package com.imagina.core_producer.service;

import com.imagina.core_producer.model.Pedido;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    public Pedido createTestPedido(){
        List<String> productos = new ArrayList<>();
        productos.add("Manzana");
        productos.add("Pera");
        productos.add("Sandia");
        productos.add("Melon");
        productos.add("Melocoton");
        productos.add("Cereza");
        productos.add("Fresa");

        List<String> clientes = new ArrayList<>();
        clientes.add("Cliente 1");
        clientes.add("Cliente 2");
        clientes.add("Cliente 3");
        clientes.add("Cliente 4");
        clientes.add("Cliente 5");
        clientes.add("Cliente 6");
        clientes.add("Cliente 7");

        List<String> productosEscogidos = new ArrayList<>();

        int indice = (int) (Math.random() * productos.size());
        for(int i=1;i<3;i++){
            productosEscogidos.add(productos.get(indice));
        }

        return Pedido.builder()
                .fechaPedido(LocalDateTime.now())
                .pedidoId(UUID.randomUUID().toString())
                .total((Math.random() * 1000) + 1)
                .cliente(clientes.get(indice))
                .productos(productosEscogidos)
                .build();
    }
}

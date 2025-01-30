package com.imagina.core_producer.controller;

import com.imagina.core_producer.model.Pedido;
import com.imagina.core_producer.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedido/v1")
public class PedidoApi {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping(value = "/all")
    public Pedido generarPedido(){
        return pedidoService.createTestPedido();
    }
}

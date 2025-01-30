package com.imagina.core_producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imagina.core_producer.model.Pedido;
import com.imagina.core_producer.producer.PedidoProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PedidoScheduler {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private PedidoProducer pedidoProducer;

    @Scheduled(fixedRate = 3000)
    public void retrievePedidos() {
        var pedidos = restTemplate.exchange("http://localhost:8080/api/pedido/v1/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Pedido>>() {
                }).getBody();

        pedidos.forEach( p ->{
            try{
                pedidoProducer.sendPedido(p);
            } catch (JsonProcessingException e){
                e.printStackTrace();
            }
        });
    }
}

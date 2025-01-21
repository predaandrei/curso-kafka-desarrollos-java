package com.imagina.kafka.api.server;

import com.imagina.kafka.api.request.OrderRequest;
import com.imagina.kafka.api.response.OrderResponse;
import com.imagina.kafka.api.command.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/order")
public class OrderApi {

    @Autowired
    private OrderService service;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        var orderNumber = service.saveOrder(request);
        var response = new OrderResponse(orderNumber);
        return ResponseEntity.ok().body(response);
    }

}
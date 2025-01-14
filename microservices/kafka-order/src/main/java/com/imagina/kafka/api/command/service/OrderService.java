package com.imagina.kafka.api.command.service;

import com.imagina.kafka.api.request.OrderRequest;
import com.imagina.kafka.broker.message.OrderMessage;
import com.imagina.kafka.command.action.OrderAction;
import com.imagina.kafka.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderAction action;

    public String saveOrder(OrderRequest request) {
        Order orderEntity = action.convertToOrder(request);

        action.saveToDatabase(orderEntity);

        orderEntity.getOrderItems().forEach(item -> {
            OrderMessage orderMessage = action.convertToOrderMessage(item);

            action.sendToKafka(orderMessage);
        });

        return orderEntity.getOrderNumber();
    }

}
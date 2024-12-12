package com.imagina.core_producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imagina.core_producer.model.Stock;
import com.imagina.core_producer.producer.StockProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

//@Service
public class StockScheduler {

    private RestTemplate restTemplate = new RestTemplate();

    //@Autowired
    private StockProducer producer;

    //@Scheduled(fixedRate = 5000)
    public void retrieveStocks() {
        var stocks = restTemplate.exchange("http://localhost:8080/api/stock/v1/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Stock>>() {
                }).getBody();

        stocks.forEach(t -> {
            try {
                producer.sendStock(t);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}

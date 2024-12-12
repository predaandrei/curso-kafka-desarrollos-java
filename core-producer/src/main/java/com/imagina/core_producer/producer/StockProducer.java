package com.imagina.core_producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imagina.core_producer.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//@Service
public class StockProducer {

    //@Autowired
    private ObjectMapper objectMapper;

    //@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendStock(Stock stock) throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(stock);
        kafkaTemplate.send("t-stocks", stock.getSymbol(), json);
    }
}

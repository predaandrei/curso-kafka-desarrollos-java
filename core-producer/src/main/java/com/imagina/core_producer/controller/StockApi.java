package com.imagina.core_producer.controller;

import com.imagina.core_producer.model.Stock;
import com.imagina.core_producer.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock/v1")
public class StockApi {

    @Autowired
    private StockService stockService;

    @GetMapping(value = "/all")
    public List<Stock> generateAllStocks() {
        return stockService.createTestStocks();
    }
}

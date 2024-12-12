package com.imagina.core_producer.service;

import com.imagina.core_producer.model.Stock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class StockService {

    private static final Map<String, Stock> STOCK_MAP = new HashMap<>();
    private static final String APPLE = "APPL";
    private static final String META = "META";
    private static final double MAX_ADJUSTMENT = 250.05d;
    private static final double MIN_ADJUSTMENT = 100.95d;

    static {
        var timestamp = System.currentTimeMillis();

        STOCK_MAP.put(APPLE, new Stock(APPLE, "Apple", 4_834.57, timestamp));
        STOCK_MAP.put(META, new Stock(META, "Meta", 1_185.29, timestamp));
    }

    public List<Stock> createTestStocks() {
        var result = new ArrayList<Stock>();
        STOCK_MAP.keySet().forEach(c -> result.add(createTestStock(c)));

        return result;
    }

    public Stock createTestStock(String name) {
        if (!STOCK_MAP.keySet().contains(name)) {
            throw new IllegalArgumentException("No existe esta acción : " + name);
        }

        var stock = STOCK_MAP.get(name);
        var price = stock.getPrice();
        var newPrice = price * ThreadLocalRandom.current().nextDouble(MIN_ADJUSTMENT, MAX_ADJUSTMENT);

        stock.setPrice(newPrice);
        stock.setTimestamp(System.currentTimeMillis());

        return stock;
    }

}

package com.imagina.core_producer.model;

public class Stock {
    private String symbol;
    private String companyName;
    private double price;
    private long timestamp;

    public Stock() {
    }

    public Stock(String symbol, String companyName, double price, long timestamp) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.setPrice(price);
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = Math.round(price * 100.0) / 100.0;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

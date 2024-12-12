package com.imagina.core_producer.model;

public class Stock {
    private  String symbol;
    private  String companyName;
    private double price;
    private long timestamp;

    public Stock(String symbol, String companyName, double price, long timestamp) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.setPrice(price);
        this.timestamp = timestamp;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPrice(double price) {
        this.price = Math.round(price * 100.0)/100.0;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

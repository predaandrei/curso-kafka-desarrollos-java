package com.imagina.core_producer.model;

public class Stock {
<<<<<<< HEAD
    private  String symbol;
    private  String companyName;
    private double price;
    private long timestamp;

=======
    private String symbol;
    private String companyName;
    private double price;
    private long timestamp;

    public Stock() {
    }

>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
    public Stock(String symbol, String companyName, double price, long timestamp) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.setPrice(price);
        this.timestamp = timestamp;
    }

<<<<<<< HEAD
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

=======
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
    public String getSymbol() {
        return symbol;
    }

<<<<<<< HEAD
=======
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
    public String getCompanyName() {
        return companyName;
    }

<<<<<<< HEAD
=======
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
    public double getPrice() {
        return price;
    }

<<<<<<< HEAD
    public long getTimestamp() {
        return timestamp;
    }
=======
    public void setPrice(double price) {
        this.price = Math.round(price * 100.0) / 100.0;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
}

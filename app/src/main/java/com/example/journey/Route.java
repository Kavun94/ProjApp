package com.example.journey;

public class Route {
    private String from;
    private String to;
    private String time;
    private int price;
    private String transportType;


    public Route() {}


    public Route(String from, String to, String time, int price, String transportType) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.price = price;
        this.transportType = transportType;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }

    public String getTransportType() {
        return transportType;
    }
}


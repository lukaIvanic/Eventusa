package com.example.eventusajava.network;

public enum RequestType {

    GET("GET"),
    POST("POST");

    private final String label;

    RequestType(String label) {
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}

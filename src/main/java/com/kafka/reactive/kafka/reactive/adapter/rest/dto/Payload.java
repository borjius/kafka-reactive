package com.kafka.reactive.kafka.reactive.adapter.rest.dto;

public class Payload {

   private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Payload{" +
            "name='" + name + '\'' +
            '}';
    }
}

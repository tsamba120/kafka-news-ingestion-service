package com.newsstream.newsingestionservice.commands;

import java.lang.reflect.Field;

public class TestRequest {
    public static void main(String[] args) throws IllegalAccessException {
        ProduceNewsEventsRequest test = new ProduceNewsEventsRequest();

        for (Field f : test.getClass().getDeclaredFields()) {
            System.out.println(f.getName() + ": " +  f.get(test).toString());
        }
    }

}

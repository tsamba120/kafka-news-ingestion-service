package com.newsstream.newsingestionservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Secrets {


    public static Map<String, String> secretsMap() {

        BufferedReader reader;
        Map<String, String> secretsMap;
        final String path = System.getProperty("user.dir") +  "/news-ingestion-service/src/main/resources/secrets.txt";

        secretsMap = new HashMap<>();

        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            while (line != null) {
                String[] keyValArr = line.split("=");
                secretsMap.put(keyValArr[0], keyValArr[1]);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Trouble reading file");
            System.exit(1);

        }
        return secretsMap;
    }
    public static void main(String[] args) {
        System.out.println(Secrets.secretsMap().get("apiKey"));
    }
}

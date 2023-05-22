package com.newsstream.newsingestionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties()
public class NewsIngestionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsIngestionServiceApplication.class, args);
	}

}

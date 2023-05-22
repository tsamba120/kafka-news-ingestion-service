package com.newsstream.newsingestionservice.controller;

import com.newsstream.newsingestionservice.commands.ProduceNewsEventsRequest;
import com.newsstream.newsingestionservice.api.NewsApiClient;
import com.newsstream.newsingestionservice.entities.Article;
import com.newsstream.newsingestionservice.service.NewsStreamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path="/api/v1")
public class NewsArticlesController {
    static final Logger logger = LoggerFactory.getLogger(NewsArticlesController.class);

    @Value("${topics.news.name}")
    private String newsTopic;

    private final KafkaTemplate<String, Article> kafkaProducerTemplate;

    private final NewsStreamingService newsStreamingService;

    NewsApiClient newsClient;

    @Autowired
    public NewsArticlesController(
            NewsStreamingService newsStreamingService,
            KafkaTemplate<String, Article> kafkaProducerTemplate,
            NewsApiClient newsClient
    ) {
        this.newsStreamingService = newsStreamingService;
        this.kafkaProducerTemplate = kafkaProducerTemplate;
        this.newsClient = newsClient;
    }

    @GetMapping(value="/health")
    public String health() {
        return "Application is healthy";
    }

    @PostMapping(value="/produce-events")
    public void produceNewsEvents(@RequestBody ProduceNewsEventsRequest cmd) {

        logger.info("INGESTING SPORTS EVENTS");

        this.newsStreamingService.produceNewsEvents(
                this.newsClient, cmd, this.kafkaProducerTemplate, newsTopic
        );


        newsClient.closeConnection();
    }







}

package com.newsstream.newsingestionservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.newsstream.newsingestionservice.api.NewsApiClient;
import com.newsstream.newsingestionservice.commands.ProduceNewsEventsRequest;
import com.newsstream.newsingestionservice.entities.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class NewsStreamingService {

    static final Logger logger = LoggerFactory.getLogger(NewsStreamingService.class);

    public void produceNewsEvents(
            NewsApiClient newsClient,
            ProduceNewsEventsRequest cmd,
            KafkaTemplate<String, Article> kafkaProducerTemplate,
            String newsTopic
    ) {
        // Configure API body
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("country", cmd.getCountry());
        paramMap.put("language", cmd.getLanguage());
        paramMap.put("category", cmd.getCategory());
        System.out.println(newsClient);

        try {
            newsClient.addParametersFromMap(paramMap);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            logger.error(e.getMessage(), e);
            logger.error("News API client experienced IO exception");
            System.exit(1);
        }

        // Perform GET request on API client
        JsonNode response = null;
        try {
            int httpStatus = newsClient.executeGetRequest();

            response = newsClient.readJsonResponse();

            if (httpStatus >= 200 && httpStatus < 300) {
                logger.info("Successful API Request");
            } else if (httpStatus >=400 && httpStatus < 500) {
                logger.error("Failed API Request: HTTP:" + httpStatus +" client error");
                logger.error(response.toString());
                return;
            } else if (httpStatus >=500 && httpStatus <6500) {
                logger.error("Failed API Request: HTTP:" + httpStatus +" server error");
                logger.error(response.toString());
                return;
            }

        } catch (IOException e) {
            logger.error(e.toString());
            System.exit(1);
        }

        if (response == null) {
            logger.error("API returned a null response");
            System.exit(1);
        }

//        logger.info(response.toString());
        int totalResults = response.get("totalResults").asInt();
        String nextPage = String.valueOf(response.get("nextPage"));
        /*
        TODO: use nextPage key to process paginated data
        Docs: https://newsdata.io/documentation/#pagination
        Example:
            `https://newsdata.io/api/1/news?apikey=YOUR_API_KEY&q=YOUR-QUERY&page=XXXPPPXX`
         */

        logger.info("Received " + totalResults + " articles");
        logger.info("Next page: " + nextPage);


        ArrayNode articles = (ArrayNode) response.get("results");

        for (JsonNode article : articles) {

            Article articleValue = new Article(article);

            ListenableFuture<SendResult<String, Article>> future = kafkaProducerTemplate.send(
                    newsTopic,
                    articleValue.getSource_id().toLowerCase().replaceAll("\\s+", "-"),
                    articleValue
            );

            future.addCallback(
                    result -> {
                        assert result != null;
                        logger.info("Published article=" + articleValue.getTitle()
                                + ",\n  partition=" + result.getRecordMetadata().partition()
                                + ",\n offset=" + result.getRecordMetadata().offset());
                    },
                    ex -> {
                        logger.error("Failed to publish " + articleValue.getTitle(), ex);
                    }
            );
        }
    }
}

package com.newsstream.newsingestionservice.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServers;

    @Value("${topics.news.name}")
    String topicName;

    @Value("${topics.news.partitions}")
    int topicPartitions;

    @Value("${topics.news.replicas}")
    int topicReplicas;

    // will use this to create topic on start-up and
    // gracefully handle when topic already exists
    @Bean
    public NewTopic newsArticlesTopic() {
        return TopicBuilder.name(topicName)
                .partitions(topicPartitions)
                .replicas(topicReplicas)
                .build();
    }
}

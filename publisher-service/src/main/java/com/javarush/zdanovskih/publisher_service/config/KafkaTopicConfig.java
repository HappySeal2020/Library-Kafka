package com.javarush.zdanovskih.publisher_service.config;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.javarush.zdanovskih.constant.Topics.*;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic authorCreatedTopic() {
        return TopicBuilder.name(AUTHOR_CREATED)
                .partitions(3)
                .replicas(1)
                .config("retention.ms", "604800000") // 7 days
                .build();
    }

    @Bean
    public NewTopic authorUpdatedTopic() {
        return TopicBuilder.name(AUTHOR_UPDATED)
                .partitions(3)
                .replicas(1)
                .config("retention.ms", "604800000") // 7 days
                .build();
    }

    @Bean
    public NewTopic publisherCreatedTopic() {
        return TopicBuilder.name(PUBLISHER_CREATED)
                .partitions(3)
                .replicas(1)
                .config("retention.ms", "604800000") // 7 days
                .build();
    }

    @Bean
    public NewTopic publisherUpdatedTopic() {
        return TopicBuilder.name(PUBLISHER_UPDATED)
                .partitions(3)
                .replicas(1)
                .config("retention.ms", "604800000") // 7 days
                .build();
    }

    @Bean
    public NewTopic authorCreatedDLTTopic() {
        return TopicBuilder.name(AUTHOR_CREATED_DLT)
                .partitions(3)
                .replicas(1)
                .config("retention.ms", "1209600000") // 14 days
                .build();
    }

    @Bean
    public NewTopic authorUpdatedDLTTopic() {
        return TopicBuilder.name(AUTHOR_UPDATED_DLT)
                .partitions(3)
                .replicas(1)
                .config("retention.ms", "1209600000") // 14 days
                .build();
    }

    @Bean
    public NewTopic publisherCreatedDLTTopic() {
        return TopicBuilder.name(PUBLISHER_CREATED_DLT)
                .partitions(3)
                .replicas(1)
                .config("retention.ms", "1209600000") // 14 days
                .build();
    }

    @Bean
    public NewTopic publisherUpdatedDLTTopic() {
        return TopicBuilder.name(PUBLISHER_UPDATED_DLT)
                .partitions(3)
                .replicas(1)
                .config("retention.ms", "1209600000") // 14 days
                .build();
    }
}

package com.javarush.zdanovskih.book_service.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConsumerConfig {


    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, ex) -> {
                            log.error("Sending to DLT. Topic: {}, key: {}, error: {}",
                                    record.topic(),
                                    record.key(),
                                    ex.getMessage());

                            return new TopicPartition(
                                    record.topic() + ".DLT",
                                    record.partition()
                            );
                        }
                );

        FixedBackOff backOff = new FixedBackOff(
                2000L, // retry interval 2 sec
                3      // retry attempts
        );

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(recoverer, backOff);

        errorHandler.setRetryListeners(
                (record, ex, deliveryAttempt) ->
                        log.warn("Retry attempt {} for record {} due to {}",
                                deliveryAttempt,
                                record.value(),
                                ex.getMessage())
        );
        return errorHandler;
    }

}
package com.javarush.zdanovskih.publisher_service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import org.testcontainers.utility.DockerImageName;

@Testcontainers

@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("test-db")
                    .withUsername("test")
                    .withPassword("test");

    //@Container
    //static final KafkaContainer kafka =
    //        new KafkaContainer(
    //                DockerImageName.parse("apache/kafka:4.0.0")
    //        );

    static final  KafkaContainer kafka;
    static {
        kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:4.0.0"));
        kafka.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {

        // PostgreSQL
        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );

        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );

        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );

        // Kafka
        registry.add(
                "spring.kafka.bootstrap-servers",
                kafka::getBootstrapServers
        );

        // JPA
        registry.add(
                "spring.jpa.hibernate.ddl-auto",
                () -> "create-drop"
        );

        registry.add(
                "spring.kafka.consumer.auto-offset-reset",
                () -> "earliest"
        );
    }

}

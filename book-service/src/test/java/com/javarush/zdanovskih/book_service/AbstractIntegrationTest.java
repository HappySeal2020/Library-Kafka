package com.javarush.zdanovskih.book_service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
public abstract class AbstractIntegrationTest {
    //@Container
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("library")
                    .withUsername("test")
                    .withPassword("test");

    //@Container
    static final KafkaContainer kafka = new KafkaContainer(
            //DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
                    //.asCompatibleSubstituteFor("apache/kafka"));
            DockerImageName.parse("apache/kafka:3.7.0"));

    static {
        postgres.start();
        kafka.start();
    }
    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

}




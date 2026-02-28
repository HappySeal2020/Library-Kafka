package com.javarush.zdanovskih.publisher_service;

import org.springframework.boot.SpringApplication;

public class TestPublisherServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(PublisherServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

package com.javarush.zdanovskih.author_service;

import org.springframework.boot.SpringApplication;

public class TestAuthorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(AuthorServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

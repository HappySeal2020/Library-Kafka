package com.javarush.zdanovskih.book_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.javarush.zdanovskih.constant.Mapping.REST_MAP;

@SpringBootApplication
public class BookServiceApplication {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", REST_MAP);
		SpringApplication.run(BookServiceApplication.class, args);
	}

}

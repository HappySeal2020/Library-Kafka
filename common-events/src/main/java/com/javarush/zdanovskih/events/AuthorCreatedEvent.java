package com.javarush.zdanovskih.events;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCreatedEvent {

    private Long id;
    private String name;
}
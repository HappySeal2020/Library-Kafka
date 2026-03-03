package com.javarush.zdanovskih.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorUpdatedEvent {
    private Long id;
    private String name;
}

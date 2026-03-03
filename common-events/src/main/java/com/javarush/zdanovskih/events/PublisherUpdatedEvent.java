package com.javarush.zdanovskih.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherUpdatedEvent {
    private Long id;
    private String name;
    private String site;
}

package com.javarush.zdanovskih.book_service.cache;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="publisher_cache")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherCache {

    @Id
    private Long id;
    @NotEmpty(message="Publisher name is mandatory field")
    @NotNull(message="Publisher name is mandatory field")
    @Column(unique = true)
    private String name;
    private String site;
}
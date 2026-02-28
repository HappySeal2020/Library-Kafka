package com.javarush.zdanovskih.book_service.cache;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name="author_cache")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCache {

    @Id
    private Long id;
    @NotNull(message="Author name is mandatory field")
    @NotEmpty(message="Author name is mandatory field")
    @Column(unique=true)
    private String name;
}
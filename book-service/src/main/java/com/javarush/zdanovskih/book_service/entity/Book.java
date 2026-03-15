package com.javarush.zdanovskih.book_service.entity;

import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="book")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message="Book name is mandatory field")
    @NotNull(message="Book name is mandatory field")
    @Column(unique=true)
    private String name;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name="author_to_book", joinColumns = @JoinColumn(name="book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="author_id", referencedColumnName = "id"))
    private List<AuthorCache> authors = new ArrayList<>();

    @Min(value = 1900, message = "Print year must be greater 1900")
    private int printYear;

    @ManyToOne @JoinColumn (name="publisher_id")
    private PublisherCache publisher;

    private String bbk;

    private String isbn;

    @Min(value=1, message = "Number of pages must be positive")
    private int pages;
}
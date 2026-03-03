package com.javarush.zdanovskih.publisher_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="publisher")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Publisher name is mandatory field")
    @NotBlank(message = "Publisher name is mandatory field")
    @Column(unique=true)
    private String name;
    private String site;
    //private boolean used;
}
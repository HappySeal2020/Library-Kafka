package com.javarush.zdanovskih.author_service.specification;
import com.javarush.zdanovskih.author_service.entity.Author;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
@Slf4j

public class AuthorSpecification {
    public static Specification<Author> filter(String name) {
        log.info("Filtering Author by {}", name);
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

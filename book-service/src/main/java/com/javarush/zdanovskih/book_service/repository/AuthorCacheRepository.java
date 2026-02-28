package com.javarush.zdanovskih.book_service.repository;

import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorCacheRepository extends JpaRepository<AuthorCache, Long> {
}

package com.javarush.zdanovskih.book_service.repository;

import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorCacheRepository extends JpaRepository<AuthorCache, Long> {
    @Query("""
       select a from AuthorCache a
       where EXISTS (
       SELECT 1 FROM Book b
       JOIN b.authors ba
       WHERE ba = a
       )
       """)
    List<AuthorCache> findAuthorsWithBooks();
}

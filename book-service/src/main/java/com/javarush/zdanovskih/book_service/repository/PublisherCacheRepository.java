package com.javarush.zdanovskih.book_service.repository;

import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublisherCacheRepository extends JpaRepository<PublisherCache, Long> {

    @Query("""
       select a from PublisherCache a
       where EXISTS (
       SELECT 1 FROM Book b
       JOIN b.publisher ba
       WHERE ba = a
       )
       """)
    List<PublisherCache> findPublishersWithBooks();
}

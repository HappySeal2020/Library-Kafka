package com.javarush.zdanovskih.book_service.repository;
import com.javarush.zdanovskih.book_service.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
//public interface BookRepository extends JpaRepository<Book, Long> {
public interface BookRepository extends JpaRepository<Book, Long> , JpaSpecificationExecutor<Book> {
}
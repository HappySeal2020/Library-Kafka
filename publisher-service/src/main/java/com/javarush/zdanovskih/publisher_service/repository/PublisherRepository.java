package com.javarush.zdanovskih.publisher_service.repository;

import com.javarush.zdanovskih.publisher_service.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PublisherRepository extends JpaRepository<Publisher, Long> , JpaSpecificationExecutor<Publisher> {
}


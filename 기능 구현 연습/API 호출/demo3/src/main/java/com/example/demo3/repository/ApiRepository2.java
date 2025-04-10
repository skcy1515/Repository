package com.example.demo3.repository;

import com.example.demo3.entity.ApiEntity2;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository2 extends ReactiveMongoRepository<ApiEntity2, String> {
}
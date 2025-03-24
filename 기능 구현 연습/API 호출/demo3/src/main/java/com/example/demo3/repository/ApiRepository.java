package com.example.demo3.repository;

import com.example.demo3.entity.ApiEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

// MongoDB에 비동기로 데이터를 저장하거나 조회하는 기능을 자동으로 제공하는 인터페이스
@Repository
public interface ApiRepository extends ReactiveMongoRepository<ApiEntity, String> {
}

package com.example.demo.repository;

import com.example.demo.entity.ImageMemoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMemoRepository extends MongoRepository<ImageMemoEntity, String> {
}

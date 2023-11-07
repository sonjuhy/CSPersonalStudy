package com.study.CSStudy.db.repository;

import com.study.CSStudy.db.entity.TestCollectionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoDBRepository extends MongoRepository<TestCollectionEntity, Long> {
    List<TestCollectionEntity> findByName(String name);
    void deleteByName(String name);
}

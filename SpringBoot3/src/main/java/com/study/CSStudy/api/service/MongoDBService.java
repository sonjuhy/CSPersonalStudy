package com.study.CSStudy.api.service;

import com.study.CSStudy.api.dto.TestCollectionDto;
import com.study.CSStudy.db.entity.TestCollectionEntity;

import java.util.List;

public interface MongoDBService {
    TestCollectionEntity insert(TestCollectionDto dto);
    List<TestCollectionEntity> findAll();
    List<TestCollectionEntity> findByName(String name);
    void deleteByName(String name);
}

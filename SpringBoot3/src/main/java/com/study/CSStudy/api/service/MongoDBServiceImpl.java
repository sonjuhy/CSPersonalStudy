package com.study.CSStudy.api.service;

import com.study.CSStudy.api.dto.TestCollectionDto;
import com.study.CSStudy.db.entity.TestCollectionEntity;
import com.study.CSStudy.db.repository.MongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoDBServiceImpl implements MongoDBService{

    @Autowired
    MongoDBRepository repository;

    @Override
    public TestCollectionEntity insert(TestCollectionDto dto) {
        return repository.insert(dto.dtoToEntity());
    }

    @Override
    public List<TestCollectionEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public List<TestCollectionEntity> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public void deleteByName(String name) {
        repository.deleteByName(name);
    }
}

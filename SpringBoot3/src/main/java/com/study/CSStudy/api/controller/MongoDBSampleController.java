package com.study.CSStudy.api.controller;

import com.study.CSStudy.api.dto.TestCollectionDto;
import com.study.CSStudy.api.service.MongoDBService;
import com.study.CSStudy.db.entity.TestCollectionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongo")
public class MongoDBSampleController {

    @Autowired
    MongoDBService service;

    @GetMapping("/findByName/{name}")
    public List<TestCollectionEntity> findByName(@PathVariable String name){
        return service.findByName(name);
    }

    @GetMapping("/findAll")
    public List<TestCollectionEntity> findAll(){
        return service.findAll();
    }

    @PostMapping("/insert")
    public void insert(@RequestBody TestCollectionDto dto){
        service.insert(dto);
    }
}

package com.study.CSStudy.api.controller;

import com.study.CSStudy.api.service.LargeDataToDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/largeData")
public class LargeDataSampleController {

    @Autowired
    LargeDataToDBService service;

    @GetMapping("/run/{mode}")
    public void run(@PathVariable int mode){
        service.run(mode);
    }
}

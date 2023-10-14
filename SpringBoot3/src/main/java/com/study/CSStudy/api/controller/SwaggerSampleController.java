package com.study.CSStudy.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/swaggerAPI")
public class SwaggerSampleController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @GetMapping
    public String getMapping(){
        log.info("getMapping method");
        return "getMapping method";
    }

    @GetMapping("/{num}")
    public String getMapping(@PathVariable int num){
        return "getMapping method : " + num;
    }

    @PostMapping
    public String postMapping(){
        return "postMapping method";
    }

    @PutMapping
    public String putMapping(){
        return "putMapping method";
    }

    @DeleteMapping
    public String deleteMapping(){
        return "deleteMapping method";
    }
}

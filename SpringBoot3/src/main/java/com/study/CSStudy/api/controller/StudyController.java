package com.study.CSStudy.api.controller;

import com.study.CSStudy.api.service.StudyInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/study")
public class StudyController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static boolean status = false;

    @GetMapping("/threadPool")
    public void threadPoolTest(){
        log.info("status : " + status);
    }

    @GetMapping("/changeStatus")
    public void changeStatus(){
        log.info("Change Status value!");
        status = !status;
    }

//    @Autowired // [error] Caused by : required a single bean, but 2 were found (StudyImpl, StudyImplOverride)
//    StudyInterface study;

    @GetMapping("/getMapping")
    public String getMappingFun(){
        return "hello?";
    }

//    @GetMapping("/getMapping") // [error] Caused by : exist mapping add (/getMapping)
//    public String getMappingOverriding(){
//        return "hello? overriding!";
//    }
}

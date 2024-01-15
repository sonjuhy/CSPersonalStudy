package com.study.CSStudy.studySpring.api.controller;

import com.study.CSStudy.studySpring.api.dto.SampleDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sampleController")
public class SampleController {

    @GetMapping("/testController")
    public @ResponseBody SampleDto testController(){
        return new SampleDto(1, "name");
    }

    /**
     * 404 Not Found 에러 발생
     * */
    @GetMapping("/testWithOutResponseBodyController")
    public SampleDto testWithOutResponseBodyController(){
        return new SampleDto(1, "name");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/testRequestMapping")
    public String requestMappingTest(){
        return "requestMappingGetTest";
    }

}

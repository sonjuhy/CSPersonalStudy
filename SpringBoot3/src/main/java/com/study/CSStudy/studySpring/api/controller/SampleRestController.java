package com.study.CSStudy.studySpring.api.controller;

import com.study.CSStudy.studySpring.api.dto.SampleDto;
import com.study.CSStudy.studySpring.api.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller Annotation 에서 ResponseBody Annotation 이 추가 된 타입
 * 그래서 객체 데이터를 반환할 때 JSON 타입으로 별도의 @ResponseBody 선언 없이 바로 리턴 가능(testRestController 메소드 참조)
 * */
@RestController
@RequestMapping("/sampleRest")
public class SampleRestController {

    @Autowired
    private SampleService service;

    @GetMapping("/testRestController")
    public SampleDto testRestController(){
        return new SampleDto(1, "name");
    }

    @GetMapping("/testParam")
    public void testRequestParam(@RequestParam("param") String param){
        /* link : localhost:8080//sampleRest/testParam?param=data
        *  body : null
        * */
        System.out.println(param);
    }

    @GetMapping("/testPathValue/{value}")
    public void testPathValue(@PathVariable("value") String value){
        /* link : localhost:8080//sampleRest/testParam/(value)
         * body : null
         * */
        System.out.println(value);
    }
    @PostMapping("/testRequestBody")
    public void testRequestBody(@RequestBody SampleDto dto){
        /* link : localhost:8080/sampleRest/testRequestBody
         * body : Object(dto)
         * */
        System.out.println(dto.getId()+", "+dto.getName());
    }

    @GetMapping("/testResponseBody")
    public SampleDto testResponse(){
        /*
        * 만약 RestController 가 아닌 Controller 였다면 public @ResponseBody SampleDto testResponse() 로 선언 해야함.
        * */
        return new SampleDto(1, "name");
    }

    @GetMapping("/testBean")
    public void testBean(){
        service.testBean();
    }

    @GetMapping("/testConfiguration")
    public void testConfiguration(){
        service.testConfiguration();
    }
}

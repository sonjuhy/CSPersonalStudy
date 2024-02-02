package com.study.CSStudy.api.controller;

import com.study.CSStudy.api.service.AsyncServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/async")
public class AsyncController {

    @Autowired
    AsyncServiceImpl asyncService;

    @GetMapping("/test")
    public void test(){
        asyncService.secondAsyncTest();
//        List<Integer> listOne = new ArrayList<>();
//        List<Integer> listTwo = new ArrayList<>();
//        List<Integer> listThree = new ArrayList<>();
//        for(int i=0;i<10;i++) {
//            listOne.add(i);
//            listTwo.add(i+10);
//            listThree.add(i+20);
//        }
//        List<List<Integer>> totalList = new ArrayList<>();
//        totalList.add(listOne);
//        totalList.add(listTwo);
//        totalList.add(listThree);
//        for(List<Integer> tmpList : totalList){
//            CompletableFuture<String> futureResult = asyncService.parallelProcessing(tmpList.toString(), tmpList);
//            futureResult.thenAccept(System.out::println);
//        }
    }
}

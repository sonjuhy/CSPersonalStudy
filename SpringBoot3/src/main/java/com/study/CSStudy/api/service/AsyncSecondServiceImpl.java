package com.study.CSStudy.api.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncSecondServiceImpl {

    @Async
    public CompletableFuture<String> parallelProcessing(String id, List<Integer> list){
        try{
            for(int i : list){
                System.out.println(i);
                Thread.sleep(1000);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return CompletableFuture.completedFuture(id);
    }
}

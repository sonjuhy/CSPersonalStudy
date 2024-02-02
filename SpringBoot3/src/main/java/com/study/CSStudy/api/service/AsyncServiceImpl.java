package com.study.CSStudy.api.service;

import com.study.CSStudy.api.dto.FileDto;
import com.study.CSStudy.db.entity.FileEntity;
import com.study.CSStudy.db.repository.LargeDataCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Service
public class AsyncServiceImpl {
    @Autowired
    LargeDataCustomRepository customRepository;

    @Autowired
    AsyncSecondServiceImpl asyncSecondService;

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Integer> saveWithBatchUpdateAsync(int id, List<FileEntity> fileList){
//        for(int i=0;i<5;i++){
//            try {
//                System.out.println(id+" async task start");
//                Thread.sleep(3000);
//                System.out.println(id+" async task end");
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
        System.out.println(id+" is started!");
        customRepository.saveAll(fileList);

        return CompletableFuture.completedFuture(id);
    }

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

    public void secondAsyncTest(){
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

        List<Integer> totalList = new ArrayList<>();
        for(int i=0;i<100;i++) totalList.add(i);

        int divNum = 10;
        int partitionSize = (int) Math.ceil((double) totalList.size() / divNum);
        System.out.println("partition size : "+partitionSize);
        List<List<Integer>> groups = IntStream.range(0, divNum)
                .mapToObj(i -> totalList
                        .subList(i * partitionSize,
                                Math.min((i + 1) * partitionSize,
                                        totalList.size()
                                )
                        )
                )
                .toList();

        for(List<Integer> tmpList : groups){
            CompletableFuture<String> futureResult = asyncSecondService.parallelProcessing(tmpList.toString(), tmpList);
            futureResult.thenAccept(System.out::println);
        }
    }
}

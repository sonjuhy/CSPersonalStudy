package com.study.CSStudy.api.service;

import com.study.CSStudy.api.dto.FileDto;
import com.study.CSStudy.db.entity.FileEntity;
import com.study.CSStudy.db.repository.LargeDataCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class AsyncServiceImpl {
    @Autowired
    LargeDataCustomRepository customRepository;
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
}

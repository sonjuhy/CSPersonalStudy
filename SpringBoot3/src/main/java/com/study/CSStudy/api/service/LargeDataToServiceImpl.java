package com.study.CSStudy.api.service;

import com.study.CSStudy.api.dto.FileDto;
import com.study.CSStudy.db.repository.LargeDataCustomRepository;
import com.study.CSStudy.db.repository.MySQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class LargeDataToServiceImpl implements LargeDataToDBService{

    private static final List<FileDto> list = new ArrayList<>();
    static int finishedCount = 0;
    @Autowired
    MySQLRepository repository;
    @Autowired
    LargeDataCustomRepository customRepository;
    @Autowired
    AsyncServiceImpl asyncService;

    @Override
    public void run(int mode) {
        if(list.isEmpty())load();
        save(mode);
    }

    @Override
    public void load() {
        try{
            BufferedReader br = new BufferedReader(new FileReader("E:\\WorkSpace\\FileList.txt"));
            StringTokenizer st;
            String str;
            while((str = br.readLine()) != null){
                st = new StringTokenizer(str);
                int id = Integer.parseInt(st.nextToken());
                String name = st.nextToken();
                int size = Integer.parseInt(st.nextToken());
                list.add(new FileDto(id, name, size));
            }
            System.out.println("Load end, list size : "+list.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(int mode) {
        switch(mode){
            case 0: // saveWithJPA
                saveWithJPA();
                break;
            case 1: // saveWithNativeQuery
                saveWithNativeQuery();
                break;
            case 2: // multi thread
                int listSize = list.size();
                int size = listSize/10;
                for(int i=0;i<listSize;i+=size){
                    int end = Math.min(i+size, list.size());
                    saveWithAsync(list.subList(i, end));
                }
                break;
            case 3:
                saveWithBatchUpdate();
                break;
            case 4:
                System.out.println("saveWithBatchUpdateAsync start.");
                long filesWalkStartTime = System.currentTimeMillis();
                List<FileDto> subList;
                int listCount = 1;

                for(int i=0;i<list.size();i=listCount*500000){
                    int end;
                    listCount++;
                    if(listCount*500000 >= list.size()) end = list.size()-1;
                    else end = listCount*500000;

                    subList = new ArrayList<>(list.subList(i, end));
                    try {
                        CompletableFuture<Integer> future = asyncService.saveWithBatchUpdateAsync(listCount - 1, subList);
                        future.thenAccept(count -> {
                                finishedCount++;
                                if(finishedCount >= 7){
                                    long filesWalkEndTime= System.currentTimeMillis();
                                    System.out.println("saveWithBatchUpdateAsync working total time : "
                                            +((filesWalkEndTime-filesWalkStartTime)/60000)+"m "
                                            +((filesWalkEndTime-filesWalkStartTime)/1000%60)+"s"
                                    );
                                }
                        });
                    }
                    catch (Exception e){

                    }
                }

                break;
            case 5: // total seq
                deleteAll();
                saveWithJPA();
                deleteAll();
                saveWithJPA();
                deleteAll();
                break;
            case -1: // delete All
                deleteAll();
                break;
        }
    }
    private void deleteAll(){
        repository.deleteAll();
    }
    private void saveWithJPA(){
        System.out.println("saveWithJPA Start");
        long filesWalkStartTime = System.currentTimeMillis();
        for(FileDto fileDto : list){
            repository.insertFileIfNotExists(fileDto.getName(), fileDto.getSize());
        }
        long filesWalkEndTime= System.currentTimeMillis();
        System.out.println("saveWithJPA working total time : "+((filesWalkEndTime-filesWalkStartTime)/60000)+"m "+((filesWalkEndTime-filesWalkStartTime)/1000%60)+"s");
    }
    private void saveWithNativeQuery(){
        System.out.println("saveWithNativeQuery start. list size : "+list.size());
        long filesWalkStartTime = System.currentTimeMillis();
        for(FileDto fileDto : list){
            repository.insertIfNotExist(fileDto.getName(), fileDto.getSize());
        }
        long filesWalkEndTime= System.currentTimeMillis();
        System.out.println("saveWithNativeQuery working total time : "+((filesWalkEndTime-filesWalkStartTime)/60000)+"m "+((filesWalkEndTime-filesWalkStartTime)/1000%60)+"s");
    }
    private void saveWithBatchUpdate(){
        System.out.println("saveWithBatchUpdate start.");
        long filesWalkStartTime = System.currentTimeMillis();
        customRepository.saveAll(list.subList(0,140000));
        long filesWalkEndTime= System.currentTimeMillis();
        System.out.println("saveWithBatchUpdate working total time : "+((filesWalkEndTime-filesWalkStartTime)/60000)+"m "+((filesWalkEndTime-filesWalkStartTime)/1000%60)+"s");
    }


    @Async
    void saveWithAsync(List<FileDto> dataList){
        for(FileDto fileDto : dataList){
            repository.insertIfNotExist(fileDto.getName(), fileDto.getSize());
        }
    }

}

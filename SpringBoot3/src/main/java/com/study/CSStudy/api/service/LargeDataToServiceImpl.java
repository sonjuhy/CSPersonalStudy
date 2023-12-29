package com.study.CSStudy.api.service;

import com.study.CSStudy.api.dto.FileDto;
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
import java.util.stream.Collectors;

@Service
public class LargeDataToServiceImpl implements LargeDataToDBService{

    private static List<FileDto> list = new ArrayList<>();
    @Autowired
    MySQLRepository repository;

    @Override
    public void run(int mode) {
        load();
        save(mode);
    }

    @Override
    public void load() {
        try{
            BufferedReader br = new BufferedReader(new FileReader("D:\\WorkSpace\\FileList.txt"));
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
            case 3: // total seq
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
        System.out.println("saveWithJPA working total time : "+((filesWalkEndTime-filesWalkStartTime)/60000)+"m "+((filesWalkEndTime-filesWalkStartTime)%60000)+"s");
    }
    private void saveWithNativeQuery(){
        System.out.println("saveWithNativeQuery start. list size : "+list.size());
        long filesWalkStartTime = System.currentTimeMillis();
        for(FileDto fileDto : list){
            repository.insertIfNotExist(fileDto.getName(), fileDto.getSize());
        }
        long filesWalkEndTime= System.currentTimeMillis();
        System.out.println("saveWithNativeQuery working total time : "+((filesWalkEndTime-filesWalkStartTime)/60000)+"m "+((filesWalkEndTime-filesWalkStartTime)%60000)+"s");
    }

    @Async
    void saveWithAsync(List<FileDto> dataList){
        for(FileDto fileDto : dataList){
            repository.insertIfNotExist(fileDto.getName(), fileDto.getSize());
        }
    }
}

package com.study.CSStudy.api.service;

import com.study.CSStudy.db.entity.FileEntity;
import com.study.CSStudy.db.repository.MySQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileSearchServiceImpl implements FileSearchService {

    private final String defaultPath = "E:\\WorkSpace";

    @Autowired
    private MySQLRepository repository;

    @Override
    public void filesWalk() {
        long filesWalkStartTime = System.currentTimeMillis();
        Path dirPath = Paths.get(defaultPath);
        List<Path> listPath;
        try{
            Stream<Path> walk = Files.walk(dirPath);
            listPath = walk.collect(Collectors.toList());
            System.out.println("walk end, list size : "+listPath.size());
            tmpWrite(listPath);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        long filesWalkEndTime= System.currentTimeMillis();
        System.out.println("filesWalk working total time : "+((filesWalkEndTime-filesWalkStartTime)/60000)+"m "+((filesWalkEndTime-filesWalkStartTime)%60000)+"s");
    }

    private void tmpWrite(List<Path> list){
        File file = new File("E:\\WorkSpace\\Intelij-workspace\\CSPersonalStudy\\SpringBoot3", "FileList.txt");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for(int i=0;i<list.size();i++){
                Path path = list.get(i);
                File tmpFile = new File(path.toString());
                String pathStr = UUID.nameUUIDFromBytes(tmpFile.getPath().getBytes(StandardCharsets.UTF_8)).toString();
                int size = (int) (file.length()/1024);
                bw.write((i+1)+" "+pathStr+" "+size+"\n");
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void filesWalkWithDB() {
        long filesWalkStartTime = System.currentTimeMillis();
        Path dirPath = Paths.get(defaultPath);
        List<Path> listPath;
        try{
            Stream<Path> walk = Files.walk(dirPath);
            listPath = walk.collect(Collectors.toList());
            long filesWalkEndTime= System.currentTimeMillis();
            System.out.println("walk end, list size : "+listPath.size()+", time(s) : "+(filesWalkEndTime-filesWalkStartTime)/1000);
            for(Path path : listPath){
                File file = new File(path.toString());
                String pathStr = UUID.nameUUIDFromBytes(file.getPath().getBytes(StandardCharsets.UTF_8)).toString();
                int size = (int) (file.length()/1024);
                repository.insertFileIfNotExists(pathStr, size);
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        long filesWalkTotalEndTime = System.currentTimeMillis();
        System.out.println("filesWalk working total time : "+((filesWalkTotalEndTime-filesWalkStartTime)/60000)+"m "+((filesWalkTotalEndTime-filesWalkStartTime)%60000)+"s");
    }

    @Override
    public void filesWalkWithDBNativeQuery() {
        long filesWalkStartTime = System.currentTimeMillis();
        Path dirPath = Paths.get(defaultPath);
        List<Path> listPath;
        try{
            Stream<Path> walk = Files.walk(dirPath);
            listPath = walk.collect(Collectors.toList());
            long filesWalkEndTime= System.currentTimeMillis();
            System.out.println("walk end, list size : "+listPath.size()+", time(s) : "+(filesWalkEndTime-filesWalkStartTime)/1000);
            for(Path path : listPath){
                File file = new File(path.toString());
                String pathStr = UUID.nameUUIDFromBytes(file.getPath().getBytes(StandardCharsets.UTF_8)).toString();
                int size = (int) (file.length()/1024);
                repository.insertIfNotExist(pathStr, size);
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        long filesWalkTotalEndTime = System.currentTimeMillis();
        System.out.println("filesWalk working total time : "+((filesWalkTotalEndTime-filesWalkStartTime)/60000)+"m "+((filesWalkTotalEndTime-filesWalkStartTime)%60000)+"s");
    }

    @Override
    public void filesDFS() {
        File defaultFile = new File(defaultPath);
        File[] files = defaultFile.listFiles();
        long size = files.length;
        for(File file : files){
            size += DFS(file.getPath(), 0);
        }
        System.out.println("filesDFS total size : "+size);
    }

    @Override
    public void filesDFSWithDB() {

    }

    @Override
    public void filesDFSWithDBNativeQuery() {

    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    private int DFS(String path, int size){
        File defaultFile = new File(path);
        File[] files = defaultFile.listFiles();
        for(File file : files){
            size++;
            if(file.isDirectory()){
                size = DFS(file.getPath(), size);
            }
        }
        return size;
    }
}

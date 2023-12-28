package com.study.CSStudy.api.controller;

import com.study.CSStudy.api.service.FileSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileSearchSampleController {

    @Autowired
    private FileSearchService fileSearchService;

    @GetMapping("/totalWalks")
    public void totalWalks(){
        fileSearchService.filesWalk();
        fileSearchService.filesWalkWithDB();
        fileSearchService.filesWalkWithDBNativeQuery();
    }
    @GetMapping("/walks")
    public String walks(){
        String result = "walks end";
        fileSearchService.filesWalk();
        return result;
    }

    @GetMapping("/DFS")
    public String DFS(){
        String result = "DFS end";
        fileSearchService.filesDFS();
        return result;
    }

    @GetMapping("/walksWithDB")
    public String walksWithDB(){
        String result = "";

        return result;
    }

    @GetMapping("/DFSWithDB")
    public String DFSWithDB(){
        String result = "";

        return result;
    }
}

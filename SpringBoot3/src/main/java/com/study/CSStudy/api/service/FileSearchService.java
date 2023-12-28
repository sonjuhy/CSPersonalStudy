package com.study.CSStudy.api.service;

public interface FileSearchService {
    void filesWalk();
    void filesWalkWithDB();
    void filesWalkWithDBNativeQuery();
    void filesDFS();
    void filesDFSWithDB();
    void filesDFSWithDBNativeQuery();
    void deleteAll();
}

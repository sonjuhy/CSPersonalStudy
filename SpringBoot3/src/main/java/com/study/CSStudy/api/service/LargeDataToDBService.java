package com.study.CSStudy.api.service;

public interface LargeDataToDBService {
    void run(int mode);
    void load();
    void save(int mode);
}

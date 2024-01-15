package com.study.CSStudy.studySpring.api.dto;

import lombok.Getter;

@Getter
public class SampleDto {
    private int id;
    private String name;

    public SampleDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

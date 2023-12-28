package com.study.CSStudy.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {
    private int id;
    private String name;
    private int size;

    public FileDto(int id, String name, int size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }
}

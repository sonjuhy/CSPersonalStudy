package com.study.CSStudy.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Table(name = "file")
@ToString
@NoArgsConstructor
public class FileEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "file_size")
    private int size;

    public FileEntity(String name, int size) {
        this.name = name;
        this.size = size;
    }
}

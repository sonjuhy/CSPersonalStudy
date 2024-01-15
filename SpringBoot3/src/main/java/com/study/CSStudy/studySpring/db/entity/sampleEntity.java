package com.study.CSStudy.studySpring.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity // spring annotation 이 아님.
public class sampleEntity {
    @Id
    @Column(name = "")
    private long id;
}

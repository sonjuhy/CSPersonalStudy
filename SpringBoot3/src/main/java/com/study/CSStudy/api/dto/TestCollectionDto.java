package com.study.CSStudy.api.dto;

import com.study.CSStudy.db.entity.TestCollectionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCollectionDto {
    private long id;
    private String name;
    private String content;

    public TestCollectionEntity dtoToEntity(){
        TestCollectionEntity entity = new TestCollectionEntity(this.id, this.name, this.content);
        return entity;
    }
    public void entityToDto(TestCollectionEntity entity){
        id = entity.getId();
        name = entity.getName();
        content = entity.getContent();
    }
}

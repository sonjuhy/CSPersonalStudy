package com.study.CSStudy.api.component.batchComponent.Mapper;

import com.study.CSStudy.api.dto.FileDto;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomFileInfoMapper implements FieldSetMapper<FileDto> {
    @Override
    public FileDto mapFieldSet(FieldSet fieldSet) throws BindException {
        FileDto dto = new FileDto();
        dto.setId(Integer.parseInt(fieldSet.readString(0)));
        dto.setName(fieldSet.readString(1));
        dto.setSize(Integer.parseInt(fieldSet.readString(2)));
        return dto;
    }
}

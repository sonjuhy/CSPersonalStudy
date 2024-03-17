package com.study.CSStudy.api.component.batchComponent.Mapper;

import com.study.CSStudy.api.dto.FileDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomFileInfoRowMapper implements RowMapper<FileDto> {
    @Override
    public FileDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        FileDto dto = new FileDto();
        dto.setId(rs.getInt("id"));
        dto.setName(rs.getString("name"));
        dto.setSize(rs.getInt("file_size"));
        return dto;
    }
}

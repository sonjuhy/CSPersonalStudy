package com.study.CSStudy.db.repository;

import com.study.CSStudy.api.dto.FileDto;
import com.study.CSStudy.db.entity.FileEntity;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LargeDataCustomRepository {
    private final JdbcTemplate jdbcTemplate;
    private final int batchSize = 500000;

    @Transactional
    public void saveAll(List<FileEntity> list){
        int batchCount = 1;
        List<FileEntity> subList;
        for(int i=0;i<list.size();i=batchCount*batchSize){
            int end;
            batchCount++;
            if(batchCount*batchSize >= list.size()) end = list.size()-1;
            else end = batchCount*batchSize;

            subList = new ArrayList<>(list.subList(i, end));
            batchInsert(subList);
        }
    }
    private void batchInsert(List<FileEntity> list){
        jdbcTemplate.batchUpdate("INSERT INTO file(name, file_size) VALUE(?, ?) AS new_file ON duplicate key UPDATE name=new_file.name, file_size=new_file.file_size", new BatchPreparedStatementSetter() {
//        jdbcTemplate.batchUpdate("INSERT INTO file_sub(file_name, id, file_size) VALUE(UUID_TO_BIN(?), ?, ?) AS new_file ON duplicate key UPDATE id=new_file.id, file_size=new_file.file_size", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, list.get(i).getName());
//                ps.setString(2, String.valueOf(i));
                ps.setString(2, String.valueOf(list.get(i).getSize()));
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
        list.clear();
    }
}

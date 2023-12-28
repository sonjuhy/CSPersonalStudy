package com.study.CSStudy.db.repository;

import com.study.CSStudy.db.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MySQLRepository extends JpaRepository<FileEntity, Integer> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO file(id, name, file_size) SELECT 0, :Name, :Size FROM DUAL WHERE NOT EXISTS(SELECT id FROM file WHERE name=:Name);", nativeQuery = true)
    int insertIfNotExist(@Param("Name")String name, @Param("Size") int size);

    FileEntity findById(int id);
    boolean existsByName(String name);
    default void insertFileIfNotExists(String name, int size){
        if(!existsByName(name)){
            save(new FileEntity(name, size));
        }
    }
}

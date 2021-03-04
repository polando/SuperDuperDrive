package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    public List<File> getFilesByUserId(Integer userId);

    @Select("SELECT fileName FROM FILES WHERE userId = #{userId}")
    public List<String> getFileNamesByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    public File getFileById(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize , userid ,filedata) VALUES(#{fileName}," +
            " #{contentType}, #{fileSize}, #{userId} ,#{fileData})")
    @Options(useGeneratedKeys = true,keyProperty = "fileId")
    int AddFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int delete(Integer fileId);



}

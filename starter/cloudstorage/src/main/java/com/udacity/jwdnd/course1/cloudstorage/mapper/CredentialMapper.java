package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    public Credential getCredentialById(Integer credentialId);

    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    public List<Credential> getCredentialByUserId(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userId) VALUES(#{url}," +
            " #{username}, #{key}, #{password}, #{userId} )")
    @Options(useGeneratedKeys = true,keyProperty = "credentialId")
    int addCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    int delete(Integer credentialId);

    @Update("UPDATE CREDENTIALS SET " +
            "url = #{url}, username = #{username}, key = #{key}, password = #{password}" +
            "WHERE credentialId = #{credentialId}")
    int update(Credential credential);

}

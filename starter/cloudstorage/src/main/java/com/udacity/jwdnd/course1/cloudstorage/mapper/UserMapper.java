package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{username}")
  /*  @Results(value = {@Result(
            property = "files",
            javaType = List.class,
            column = "userId",
            many= @Many(select = "getFileByUserId"))})*/
    User getUser(String username);


    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}," +
            " #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true,keyProperty = "userId")
    int createUser(User user);

    @Delete("DELETE FROM USERS WHERE userId = #{userId}")
    void DeleteUser(Integer userId);




}

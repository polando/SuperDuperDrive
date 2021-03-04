package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    public Note getNoteById(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    public List<Note> getNotesByUserId(Integer userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}," +
            " #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true,keyProperty = "noteId")
    int AddNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
    int delete(Integer noteId);

    @Update("UPDATE NOTES SET " +
            "notetitle = #{noteTitle}, notedescription = #{noteDescription}, userid = #{userId} " +
            "WHERE noteId = #{noteId}")
    int Update(Note note);
}

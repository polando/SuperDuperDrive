package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    NoteMapper noteMapper;

    @Autowired
    UserService userService;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public boolean addNote(Note note){
        int effectedRows = 0 ;
        note.setUserId(userService.getCurrentUser().getUserId());
        effectedRows = noteMapper.AddNote(note);
        return(0 < effectedRows);
    }

    public boolean removeNote(Integer noteId){
        int effectedRows = 0 ;
        effectedRows = noteMapper.delete(noteId);
        return(0 < effectedRows);
    }

    public boolean editNote(Note note){
        int effectedRows = 0 ;
        note.setUserId(userService.getCurrentUser().getUserId());
        effectedRows = noteMapper.Update(note);
        return(0 < effectedRows);
    }


}

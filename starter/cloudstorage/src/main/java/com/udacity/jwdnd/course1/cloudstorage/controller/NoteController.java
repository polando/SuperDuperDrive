package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/note")
public class NoteController {

    NoteService noteService;
    UserService userService;


    final String pageAddress = "/home#nav-notes";

    public NoteController(NoteService noteService,UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public RedirectView addOrEditNote(Note note, RedirectAttributes redirectAttributes){
        List<String> noteTitles = userService.getUserNotes(userService.getCurrentUser().getUserId())
                .stream().map(Note::getNoteTitle)
                .collect(Collectors.toList());
        if(noteTitles.contains(note.getNoteTitle())){
            redirectAttributes.addFlashAttribute("noteTitleDuplicate", true);
        }
        else if(note.getNoteDescription().length()>999){
            redirectAttributes.addFlashAttribute("notDescTooLarge", true);
        }
        else {
            if (note.getNoteId() == null)
                noteService.addNote(note);
            else {
                noteService.editNote(note);
            }
            redirectAttributes.addFlashAttribute("noteChangeSuccess", true);
        }

        return new RedirectView(pageAddress);
    }

    @DeleteMapping
    public RedirectView removeNote(@RequestParam("noteId") Integer noteId,RedirectAttributes redirectAttributes){
        noteService.removeNote(noteId);
        redirectAttributes.addFlashAttribute("noteRemoveSuccess", true);
        return new RedirectView(pageAddress);
    }


}

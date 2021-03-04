package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/note")
public class NoteController {

    NoteService noteService;

    final String pageAddress = "/home#nav-notes";

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public RedirectView addOrEditNote(Note note, RedirectAttributes redirectAttributes){
        if(note.getNoteId() == null)
            noteService.addNote(note);
        else {
            noteService.editNote(note);
        }
        redirectAttributes.addFlashAttribute("noteChangeSuccess", true);
        return new RedirectView(pageAddress);
    }

    @DeleteMapping
    public RedirectView removeNote(@RequestParam("noteId") Integer noteId,RedirectAttributes redirectAttributes){
        noteService.removeNote(noteId);
        redirectAttributes.addFlashAttribute("noteRemoveSuccess", true);
        return new RedirectView(pageAddress);
    }


}

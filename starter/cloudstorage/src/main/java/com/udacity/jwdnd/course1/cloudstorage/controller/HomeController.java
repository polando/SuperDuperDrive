package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.FileStorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {

    UserService userService;
    FileStorageService fileStorageService;

    public HomeController(UserService userService,FileStorageService fileStorageService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        model.addAttribute("files", userService.getUserFiles(userService.getCurrentUser().getUserId()));
        model.addAttribute("notes", userService.getUserNotes(userService.getCurrentUser().getUserId()));
        model.addAttribute("credentials", userService.getUserCredentials(userService.getCurrentUser().getUserId()));
        model.addAttribute("newNote", new Note());
        model.addAttribute("newCredential", new Credential());
        return "home";
    }

}

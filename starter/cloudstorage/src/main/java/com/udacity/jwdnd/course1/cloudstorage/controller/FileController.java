package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileStorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/file")
public class FileController {

    FileService fileService;
    UserService userService;
    FileStorageService fileStorageService;

    public FileController(FileService fileService, FileStorageService fileStorageService,UserService userService) {
        this.fileService = fileService;
        this.fileStorageService = fileStorageService;
        this.userService = userService;
    }

    @DeleteMapping
    public RedirectView RemoveFile(@RequestParam("fileId") Integer fileId,RedirectAttributes redirectAttributes){
        fileService.removeFileById(fileId);
        redirectAttributes.addFlashAttribute("fileRemoveSuccess", true);
        return new RedirectView("/home");
    }

    @GetMapping
    public ResponseEntity<Resource> getFile(@RequestParam("fileId") Integer fileId){
        File file = fileService.getFileById(fileId);
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType(file.getContentType());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+file.getFileName());
        headers.setContentType(mediaType);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Long.parseLong(file.getFileSize()))
                .contentType(mediaType)
                .body(resource);
    }

    @PostMapping
    public RedirectView  handleFileUpload(@RequestParam("fileUpload") MultipartFile multipartFile, RedirectAttributes redirAttrs) {
        List<String> fileNames = userService.fileNamesForUser(userService.getCurrentUser().getUserId());
        if(multipartFile.getOriginalFilename().isBlank())
            redirAttrs.addFlashAttribute("noFile", true);

        else if (!fileNames.contains(multipartFile.getOriginalFilename())){
            redirAttrs.addFlashAttribute("fileUploadSuccess", true);
            fileStorageService.save(multipartFile);}
        else
            redirAttrs.addFlashAttribute("duplicateFileName", true);

        return new RedirectView("/home#nav-files");
    }
}

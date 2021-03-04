package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.security.Principal;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    FileMapper fileMapper;

    @Autowired
    UserService userService;

    public FileStorageServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public void save(MultipartFile file) {
        byte[] fileData;
        try {
            fileData = file.getInputStream().readAllBytes();
            fileMapper.AddFile(new File(null,file.getOriginalFilename(),file.getContentType()
                    ,Long.toString(file.getSize()),userService.getCurrentUser().getUserId(),fileData));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public File load(Integer fileId){
        return fileMapper.getFileById(fileId);
      //  ByteArrayResource resource = new ByteArrayResource(file.getFileData());
    }
}

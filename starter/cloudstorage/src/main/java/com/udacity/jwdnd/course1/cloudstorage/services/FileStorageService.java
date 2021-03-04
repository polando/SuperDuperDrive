package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {

    void save(MultipartFile file);

}

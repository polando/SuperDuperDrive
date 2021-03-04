package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public Boolean removeFileById(Integer fileId){
        int effectedRows = 0 ;
        effectedRows = fileMapper.delete(fileId);
        return(0 < effectedRows);
    }

    public File getFileById(Integer fileId){
        return fileMapper.getFileById(fileId);
    }
}

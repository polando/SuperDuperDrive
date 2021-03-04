package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {
    private UserMapper userMapper;
    private HashService hashService;
    private FileMapper fileMapper;
    private NoteMapper noteMapper;
    private CredentialMapper credentialMapper;

    public UserService(UserMapper userMapper, HashService hashService, FileMapper fileMapper, NoteMapper noteMapper, CredentialMapper credentialMapper) {
        this.userMapper = userMapper;
        this.hashService = hashService;
        this.fileMapper = fileMapper;
        this.noteMapper = noteMapper;
        this.credentialMapper = credentialMapper;
    }

    public boolean isUsernameAvailable(String username){
        return (userMapper.getUser(username) == null);
    }

    public boolean createUser(User user){
        int effectedRows = 0 ;
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        if(userMapper.getUser(user.getUsername())==null)
            effectedRows = userMapper.createUser(new User(null,user.getUsername(),encodedSalt,hashedPassword,user.getFirstName(),user.getLastName()));
        return(0 < effectedRows);
    }

    public void removeUser(User user){
         userMapper.DeleteUser(user.getUserId());
    }

    public User getCurrentUser(){
        String username="";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userMapper.getUser(username);
    }

    public List<File> getUserFiles(Integer userId){
        return fileMapper.getFilesByUserId(userId);
    }

    public List<Note> getUserNotes(Integer userId){
        return noteMapper.getNotesByUserId(userId);
    }

    public List<String> fileNamesForUser(Integer userId) { return fileMapper.getFileNamesByUserId(userId); }

    public List<Credential> getUserCredentials(Integer userId){
        return credentialMapper.getCredentialByUserId(userId);
    }

}

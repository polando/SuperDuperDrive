package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CredentialService {

    @Autowired
    UserService userService;

    @Autowired
    EncryptionService encryptionService;

    CredentialMapper credentialMapper;


    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public boolean addCredential(Credential credential){
        int effectedRows = 0 ;
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setUserId(userService.getCurrentUser().getUserId());
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
        effectedRows = credentialMapper.addCredential(credential);
        return(0 < effectedRows);
    }

    public boolean removeCredential(Integer credentialId){
        int effectedRows = 0 ;
        effectedRows = credentialMapper.delete(credentialId);
        return(0 < effectedRows);
    }

    public boolean editCredential(Credential credential){
        int effectedRows = 0 ;
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(),encodedKey);
        credential.setKey(encodedKey);
        credential.setUserId(userService.getCurrentUser().getUserId());
        credential.setPassword(encryptedPassword);
        effectedRows = credentialMapper.update(credential);
        return(0 < effectedRows);
    }

    public Credential getDecipheredCredentialById(Integer credentialId){
        Credential credential = credentialMapper.getCredentialById(credentialId);
        String plainPass = encryptionService.decryptValue(credential.getPassword(),credential.getKey());
        credential.setPassword(plainPass);
        return credential;
    }
}

package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    CredentialService credentialService;
    UserService userService;

    public CredentialController(CredentialService credentialService,UserService userService) {
        this.credentialService = credentialService;
        this.userService =userService;
    }

    @PostMapping
    public RedirectView addOrEditCredential(Credential credential, RedirectAttributes redirectAttributes){
        List<String> credentialsUsernames = userService.getUserCredentials(userService.getCurrentUser().getUserId())
                .stream()
                .filter(item -> !item.getCredentialId().equals(credential.getCredentialId()))
                .map(Credential::getUsername)
                .collect(Collectors.toList());
        if(credentialsUsernames.contains(credential.getUsername())){
            redirectAttributes.addFlashAttribute("credentialsUsernameDuplicate",true);
        }
        else {
         if (credential.getCredentialId() == null)
                credentialService.addCredential(credential);
         else {
                credentialService.editCredential(credential);
            }
            redirectAttributes.addFlashAttribute("credentialChangeSuccess",true);
        }
        return new RedirectView("/home");
    }

    @DeleteMapping
    public RedirectView removeCredential(@RequestParam("credentialId") Integer credentialId, RedirectAttributes redirectAttributes){
        credentialService.removeCredential(credentialId);
        redirectAttributes.addFlashAttribute("credentialRemoveSuccess",true);
        return new RedirectView("/home#nav-credentials");
    }

    @GetMapping
    @ResponseBody
    public Credential viewCredential(@RequestParam("credentialId") Integer credentialId){
        Credential credential = credentialService.getDecipheredCredentialById(credentialId);
        return credential;
    }

}

package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    public RedirectView addOrEditCredential(Credential credential, RedirectAttributes redirectAttributes){
        if(credential.getCredentialId() == null)
            credentialService.addCredential(credential);
        else {
            credentialService.editCredential(credential);
        }
        redirectAttributes.addFlashAttribute("credentialChangeSuccess",true);
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

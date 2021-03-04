package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignupPage(@ModelAttribute("user") User user){
        return "signup";
    }

    @PostMapping
    public String signupUser(@ModelAttribute("user") User user, Model model, RedirectAttributes redirectAttributes) {
        String signupError = null;

        if (!userService.isUsernameAvailable(user.getUsername())) {
            signupError = "Username already exist";
        }

        if(signupError == null){
            if(!userService.createUser(user)){
                signupError = "Problem during sign up. Please try again.";
            }
        }

        if (signupError == null) {
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", signupError);
        }

        return "signup";
    }

}

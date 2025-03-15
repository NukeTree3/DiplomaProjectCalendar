package com.nuketree3.example.diplomaprojectcalendar.controllers;

import com.nuketree3.example.diplomaprojectcalendar.domain.User;
import com.nuketree3.example.diplomaprojectcalendar.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

import static com.nuketree3.example.diplomaprojectcalendar.enums.Role.SecurityConstants.ROLE_NOT_ACTIVATED_STRING;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration-page";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if(error != null) {
            model.addAttribute("loginError", "true");
        }
        return "login";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model) throws SQLException {
        if(!userService.createUser(user)) {
            model.addAttribute("error", "Username or password is incorrect");
            return "registration-page";
        }
        return "redirect:/login";
    }

    @GetMapping("/activationcode/{code}")
    @PreAuthorize("hasRole('"+ROLE_NOT_ACTIVATED_STRING+"')")
    public String activationCode(@PathVariable String code, Model model) {
        boolean isActivated = userService.activateUser(code);
        if(isActivated) {
            model.addAttribute("message", "Activated" );
        }
        else {
            model.addAttribute("message", "Error" );
        }
        return "login";
    }
}

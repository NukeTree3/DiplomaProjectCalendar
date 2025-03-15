package com.nuketree3.example.diplomaprojectcalendar.controllers;

import com.nuketree3.example.diplomaprojectcalendar.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(){
        return "admin";
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminQuery(Model model, @RequestParam("Query") String query){
        model.addAttribute("queryStatus", adminService.executeArbitrarySQL(query));
        return "admin";
    }
}

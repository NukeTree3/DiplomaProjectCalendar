package com.nuketree3.example.diplomaprojectcalendar.controllers;

import com.nuketree3.example.diplomaprojectcalendar.services.DropboxService;
import com.nuketree3.example.diplomaprojectcalendar.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class ImageController {

    private DropboxService dropboxService;
    private UserService userService;

    @PostMapping("/images/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file, Principal principal) {
        try {
            dropboxService.uploadFile(file);
            userService.addUserPhotoUrl(principal.getName(), file.getOriginalFilename());
            return "redirect:/profile";
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/profile";
        }
    }

    @GetMapping("/images/download/{path}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable("path") String dropboxPath) {
        try {
            byte[] imageBytes = dropboxService.downloadFile(dropboxPath);
            if(imageBytes.length == 0){
                return null;
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

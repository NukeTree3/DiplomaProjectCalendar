package com.nuketree3.example.diplomaprojectcalendar.controllers;

import com.nuketree3.example.diplomaprojectcalendar.domain.ChatMessage;
import com.nuketree3.example.diplomaprojectcalendar.services.ChatMessageService;
import com.nuketree3.example.diplomaprojectcalendar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        chatMessageService.saveChatMessage(chatMessage);
        String receiver = chatMessage.getTo();
        messagingTemplate.convertAndSend("/user/"+receiver+"/queue/messages", chatMessage);
    }


    @GetMapping("/friend/chat/{friendUsername}")
    public String openPrivateChat(@PathVariable String friendUsername, Model model, Principal principal) {
        if(userService.getUserFriendsUser(principal.getName()).contains(friendUsername) || friendUsername.equals(principal.getName())) {
            List<ChatMessage> history = chatMessageService.getChatMessages(friendUsername, principal.getName());
            String username = principal.getName();
            model.addAttribute("username", username);
            model.addAttribute("friendUsername", friendUsername);
            model.addAttribute("history", history);
            return "chat";
        }else {
            return "friendsearchpage";
        }
    }
}

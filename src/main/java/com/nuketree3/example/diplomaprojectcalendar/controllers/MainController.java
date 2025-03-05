package com.nuketree3.example.diplomaprojectcalendar.controllers;

import com.nuketree3.example.diplomaprojectcalendar.domain.Note;
import com.nuketree3.example.diplomaprojectcalendar.domain.User;
import com.nuketree3.example.diplomaprojectcalendar.services.NoteService;
import com.nuketree3.example.diplomaprojectcalendar.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final NoteService noteService;
    private final UserService userService;
    private int year;
    private int month;

    @GetMapping("/main")
    public String Main(){
        getDate();
        return "main";
    }

    @GetMapping("/calendar/{year}/{month}")
    public String Calendar(@PathVariable int month, @PathVariable int year, Model model, Principal principal){
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("calendar", noteService.getSelfNotes(principal.getName(), month, year));
        return "calendar";
    }

    private void getDate(){
        LocalDate localDate = LocalDate.now();
        year = localDate.getYear();
        month = localDate.getMonthValue();
    }

    @GetMapping("/calendar/{date}")
    public String CalendarDate(@PathVariable LocalDate date, Model model, Principal principal){
        ArrayList<Note> selfNotes = noteService.getSelfNote(principal.getName(), date);
        model.addAttribute("user", principal.getName());
        model.addAttribute("date", date);
        model.addAttribute("notes", selfNotes);
        return "day";
    }

    @PostMapping("/calendar/{date}/add_note")
    public String CalendarAddNote(@PathVariable LocalDate date, @RequestParam("textNode") String textNode, Model model, Principal principal){
        System.out.println(principal.getName());
        Note note = new Note(date, principal.getName(), textNode);
        noteService.saveNote(note);
        return "redirect:/calendar/" + date;
    }

    @PostMapping("/calendar/{date}/{noteId}/delete_note")
    public String CalendarDelNote(@PathVariable Long noteId, @PathVariable LocalDate date, Model model, Note note){
        noteService.deleteNote(noteId);
        return "redirect:/calendar/" + date;
    }

    @PostMapping("/calendar/{date}/{note}/change_text")
    public String CalendarChangeNote(@PathVariable LocalDate date, @PathVariable int note, Model model, @RequestParam String text){
        noteService.changeNoteText(note, text);
        return "redirect:/calendar/" + date;
    }

    @GetMapping("/profile")
    public String Profile(Model model, Principal principal){
        User user = userService.getUserByUserName(principal.getName());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("birthday", user.getBirthday().toString());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("firstname", user.getFirstName());
        model.addAttribute("lastname", user.getLastName());
        model.addAttribute("photourl", userService.getUserPhotoUrlByUserID(user.getId()));
        model.addAttribute("friends", userService.getUserFriendUserID(user.getId()));
        return "profile";
    }

    @GetMapping("/friend_search")
    public String FindFriends(@RequestParam(value = "query", required = false) String query, Model model){
        model.addAttribute("users", userService.getUsersLike(query));
        return "friendsearchpage";
    }
}

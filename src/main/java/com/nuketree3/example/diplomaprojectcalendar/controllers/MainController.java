package com.nuketree3.example.diplomaprojectcalendar.controllers;

import com.nuketree3.example.diplomaprojectcalendar.domain.Note;
import com.nuketree3.example.diplomaprojectcalendar.domain.User;
import com.nuketree3.example.diplomaprojectcalendar.services.NoteService;
import com.nuketree3.example.diplomaprojectcalendar.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final NoteService noteService;
    private final UserService userService;

    @GetMapping("/")
    public String Main(Model model, Principal principal) {
        LocalDate localDate = LocalDate.now();
        if(principal != null) {
            model.addAttribute("user", principal.getName());
        }else {
            model.addAttribute("user", null);
        }
        model.addAttribute("year", localDate.getYear());
        model.addAttribute("month", localDate.getMonthValue());
        return "main";
    }

    @GetMapping("/calendar/{year}/{month}")
    public String Calendar(@RequestParam(value = "checkFriends", required = false) String[] checkFriends, @PathVariable int month, @PathVariable int year, Model model, Principal principal){
        String myPhotoUrl = userService.getUserPhotoUrlByUsername(principal.getName());
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("monthString", Month.of(month).name());
        model.addAttribute("myUserName", principal.getName());
        model.addAttribute("photourl", myPhotoUrl);
        HashMap<String, String> friends = userService.getUserFriendsWithPhotoUrl(principal.getName());
        friends.put(principal.getName(), myPhotoUrl);
        model.addAttribute("friends", friends);
        if(checkFriends == null){
            model.addAttribute("calendar", noteService.getNotes(month, year, new ArrayList<>(Collections.singletonList(principal.getName()))));
        }else{
            model.addAttribute("calendar", noteService.getNotes(month, year, new ArrayList<>(Arrays.asList(checkFriends))));
        }
        return "calendar";
    }

    @GetMapping("/calendar/{date}")
    public String CalendarDate(@PathVariable LocalDate date, Model model, Principal principal){
        ArrayList<Note> notes = noteService.getSelfNote(principal.getName(), date);
        notes.addAll(noteService.getFriendsNote(userService.getUserFriendsUser(principal.getName()), date));
        model.addAttribute("user", principal.getName());
        model.addAttribute("date", date);
        model.addAttribute("notes", notes);
        return "day";
    }

    @PostMapping("/calendar/{date}/add_note")
    public String CalendarAddNote(@PathVariable LocalDate date, @RequestParam("textNode") String textNode, Model model, Principal principal){
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
        model.addAttribute("photourl", userService.getUserPhotoUrlByUsername(user.getUsername()));
        model.addAttribute("friends", userService.getUserFriendsWithPhotoUrl(principal.getName()));
        return "profile";
    }

    @GetMapping("/friend_search")
    public String FindFriends(@RequestParam(value = "query", required = false) String queryName, Model model, Principal principal){
        model.addAttribute("users", userService.getUsersLike(queryName, principal.getName()));
        return "friendsearchpage";
    }

    @PostMapping("/friend_search/add_friend")
    public String addFriend(@RequestParam("username") String username, Principal principal){
        userService.sendFriendOffer(username, principal.getName());
        return "redirect:/friend_search";
    }

    @PostMapping("/profile/delete_friend")
    public String deleteFriend(@RequestParam("friend") String username, Principal principal){
        userService.delFriend(username, principal.getName());
        return "redirect:/profile";
    }

    @GetMapping("/profile/invitations")
    public String Invitation(Model model, Principal principal){
        model.addAttribute("invitations", userService.getInvitations(principal.getName()));
        return "invitations";
    }

    @PostMapping("/profile/invitations/accept")
    public String acceptInvitations(@RequestParam("friendUserName") String friendUserName, Principal principal){
        userService.acceptFriend(friendUserName, principal.getName());
        return "redirect:/profile/invitations";
    }

    @PostMapping("/profile/invitations/reject")
    public String rejectInvitations(@RequestParam("friendUserName") String friendUserName, Principal principal){
        userService.rejectFriend(friendUserName, principal.getName());
        return "redirect:/profile/invitations";
    }
}

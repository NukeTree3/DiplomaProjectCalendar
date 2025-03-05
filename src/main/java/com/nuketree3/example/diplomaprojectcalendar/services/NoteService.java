package com.nuketree3.example.diplomaprojectcalendar.services;

import com.nuketree3.example.diplomaprojectcalendar.domain.Calendar;
import com.nuketree3.example.diplomaprojectcalendar.domain.Day;
import com.nuketree3.example.diplomaprojectcalendar.domain.Note;
import com.nuketree3.example.diplomaprojectcalendar.domain.User;
import com.nuketree3.example.diplomaprojectcalendar.repositories.NoteRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteService{
    private final NoteRepositories noteRepositories;

//    public ArrayList<Day> getUsersNote(ArrayList<User> users, int month, int year){
//        Calendar calendar = new Calendar();
//        ArrayList<Day> days = new ArrayList<>();
//        for (LocalDate day : calendar.getMonth(month, year)) {
//            for (User user : users) {
//                ArrayList<Optional<Note>> notes = noteRepositories.getNodeByDateAndUsername(day, user.getUsername());
//                ArrayList<Note> dayNotes = new ArrayList<>();
//                System.out.println(notes.size());
//                if(!notes.isEmpty()){
//                    for(Optional<Note> note : notes){
//                        dayNotes.add(note.orElse(null));
//                    }
//                    days.add(new Day(dayNotes, day));
//                }else{
//                    days.add(new Day(null, day));
//                }
//            }
//        }
//        return days;
//    }

    public ArrayList<Day> getSelfNotes(String username, int month, int year){
        Calendar calendar = new Calendar();
        ArrayList<Day> days = new ArrayList<>();
        for (LocalDate day : calendar.getMonth(month, year)) {
            ArrayList<Optional<Note>> notes = noteRepositories.getNodeByDateAndUsername(day, username);
            ArrayList<Note> dayNotes = new ArrayList<>();
            if(!notes.isEmpty()){
                for(Optional<Note> note : notes){
                    dayNotes.add(note.orElse(null));
                }
                days.add(new Day(dayNotes, day));
            }else{
                days.add(new Day(null, day));
            }
        }
        return days;
    }

    public ArrayList<Note> getSelfNote(String username, LocalDate date){
        ArrayList<Note> notes = new ArrayList<>();
        for(Optional<Note> note : noteRepositories.getNodeByDateAndUsername(date, username)){
            notes.add(note.orElse(null));
        }
        return notes;
    }

    public void saveNote(Note note){
        noteRepositories.save(note);
    }

    public void deleteNote(Long note){
        noteRepositories.deleteById(Math.toIntExact(note));
    }

    public void changeNoteText(int id, String newText){
        noteRepositories.changeNoteText(id, newText);
    }
}

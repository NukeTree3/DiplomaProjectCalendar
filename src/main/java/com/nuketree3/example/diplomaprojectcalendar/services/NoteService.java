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
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteService{
    private final NoteRepositories noteRepositories;

    /**
     * getNotes - метод, который возвращает список всех заметок по конкретному месяцу и списку username'ов
     * @param month - месяц, по которому будет выборка
     * @param year - год, по которому идет выборка
     * @param friendsUsernames - список username'ов, по которому идет выборка
     * @return список заметок
     */
    public ArrayList<Day> getNotes(int month, int year, List<String> friendsUsernames){
        Calendar calendar = new Calendar();
        ArrayList<Day> days = new ArrayList<>();
        for (LocalDate day : calendar.getMonth(month, year)) {
            ArrayList<Note> dayNotes = new ArrayList<>();
            for (String name : friendsUsernames) {
                ArrayList<Optional<Note>> notes = noteRepositories.getNodeByDateAndUsername(day, name);
                if(!notes.isEmpty()){
                    for(Optional<Note> note : notes){
                        dayNotes.add(note.orElse(null));
                    }
                }
            }
            days.add(new Day(dayNotes, day));
        }
        return days;
    }

    /**
     * getSelfNote - метод, который возвращает список собственных заметок по дате
     * @param username - пользовательский username
     * @param date - дата, по которой идет выборка
     * @return - список заметок
     */
    public ArrayList<Note> getSelfNote(String username, LocalDate date){
        ArrayList<Note> notes = new ArrayList<>();
        for(Optional<Note> note : noteRepositories.getNodeByDateAndUsername(date, username)){
            notes.add(note.orElse(null));
        }
        return notes;
    }

    /**
     * getFriendsNote - метод, который возвращает список заметок по списку друзей и дате
     * @param friendsUsernames - список username'ов друзей
     * @param date - дата, по которой идет выбор заметок
     * @return список заметок
     */
    public ArrayList<Note> getFriendsNote(List<String> friendsUsernames, LocalDate date){
        ArrayList<Note> notes = new ArrayList<>();
        for(Optional<Note> note : noteRepositories.getNodeByDateAndUsernames(date, friendsUsernames)){
            notes.add(note.orElse(null));
        }
        return notes;
    }

    /**
     * saveNote - метод, который сохраняет заметку в базе данных
     * @param note - сохраняемая заметка
     */
    public void saveNote(Note note){
        noteRepositories.save(note);
    }

    /**
     * deleteNote - метод, который удаляет пользовательскую заметку
     * @param note - ключ заметки, которую нужно удалить
     */
    public void deleteNote(Long note){
        noteRepositories.deleteById(Math.toIntExact(note));
    }

    /**
     * changeNoteText - метод, который изменяет содержимое пользовательской заметки
     * @param id - ключ заметки, которую нужно изменить
     * @param newText - новое содержимое заметки
     */
    public void changeNoteText(int id, String newText){
        noteRepositories.changeNoteText(id, newText);
    }
}

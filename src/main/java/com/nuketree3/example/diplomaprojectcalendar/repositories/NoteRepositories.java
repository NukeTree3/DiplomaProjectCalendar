package com.nuketree3.example.diplomaprojectcalendar.repositories;

import com.nuketree3.example.diplomaprojectcalendar.domain.Note;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface NoteRepositories extends JpaRepository<Note, Integer> {
    Optional<Note> findByAuthor(String author);

    @Query(value = "SELECT * FROM date_note WHERE date = :date AND author_username = :username", nativeQuery = true)
    ArrayList<Optional<Note>> getNodeByDateAndUsername(@Param("date") LocalDate date, @Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE date_note SET text_note = :new_text WHERE id = :id", nativeQuery = true)
    void changeNoteText(@Param("id") int id, @Param("new_text") String newText);
}

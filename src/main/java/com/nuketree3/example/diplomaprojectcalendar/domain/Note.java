package com.nuketree3.example.diplomaprojectcalendar.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "date_note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "author_username")
    private String author;
    @Column(name = "text_note")
    private String text;

    public Note(LocalDate date, String author, String text) {
        this.date = date;
        this.author = author;
        this.text = text;
    }

    public Note() {

    }
}

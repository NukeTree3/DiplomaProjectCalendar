package com.nuketree3.example.diplomaprojectcalendar.domain;

import jakarta.annotation.sql.DataSourceDefinition;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
public class Day {
    private ArrayList<Note> note;
    private final LocalDate date;

    public Day(ArrayList<Note> note, LocalDate date) {
        this.note = note;
        this.date = date;
    }
}

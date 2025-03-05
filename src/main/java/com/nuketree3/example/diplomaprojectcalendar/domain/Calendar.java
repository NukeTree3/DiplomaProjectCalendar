package com.nuketree3.example.diplomaprojectcalendar.domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class Calendar {

    public ArrayList<LocalDate> getMonth(int month, int year) {
        ArrayList<LocalDate> days = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
            days.add(LocalDate.of(year, month, i));
        }
        return days;
    }
}

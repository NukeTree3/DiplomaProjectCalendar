package com.nuketree3.example.diplomaprojectcalendar.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "Lastname")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "birthdate")
    private LocalDate birthday;

    public User(String username, String password, String firstName, String lastName, String email, LocalDate birthday) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
    }

    public User() {

    }
}

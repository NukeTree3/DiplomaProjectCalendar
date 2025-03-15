package com.nuketree3.example.diplomaprojectcalendar.domain;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "from_user_id")
    private String from;
    @Column(name = "to_user_id")
    private String to;
    @Column(name = "message_text")
    private String message;
    @Column(name = "time")
    private String time;

    public ChatMessage() {
        time = LocalDateTime.now().toString();
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            System.err.println("Error converting Message to JSON: " + e.getMessage());
            return "{" +
                    "\"error\": \"Failed to convert Message to JSON.  Check your dependencies and Jackson configuration.\"" +
                    "}";
        }
    }
}

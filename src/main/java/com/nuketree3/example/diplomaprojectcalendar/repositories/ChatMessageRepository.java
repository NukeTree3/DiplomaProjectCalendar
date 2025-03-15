package com.nuketree3.example.diplomaprojectcalendar.repositories;

import com.nuketree3.example.diplomaprojectcalendar.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = "SELECT * FROM messages WHERE (from_user_id = :username1 AND " +
            "to_user_id = :username2) OR (from_user_id = :username2 " +
            "AND to_user_id = :username1) ORDER BY time ", nativeQuery = true)
    List<ChatMessage> getChatMessagesByUsernames(@Param("username1") String username1, @Param("username2") String username2);
}

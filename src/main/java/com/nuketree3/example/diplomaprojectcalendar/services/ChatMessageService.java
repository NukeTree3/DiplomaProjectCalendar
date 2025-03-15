package com.nuketree3.example.diplomaprojectcalendar.services;

import com.nuketree3.example.diplomaprojectcalendar.domain.ChatMessage;
import com.nuketree3.example.diplomaprojectcalendar.repositories.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    /**
     * getChatMessages - метод, который вызывает метод репозитория и возвращает историю диалога между 2мя пользователями
     * @param username1 - username первого пользователя
     * @param username2 - username - второго пользователя
     * @return - возвращает список сообщений двух пользователей
     */
    public List<ChatMessage> getChatMessages(String username1, String username2) {
        return chatMessageRepository.getChatMessagesByUsernames(username1, username2);
    }

    /**
     * saveChatMessage - метод, который вызывает метод сохранения сообщения
     * @param chatMessage - экземпляр класса пользовательского сообщения
     */
    public void saveChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    /**
     * deleteChatMessage - метод, который вызывает метод удаления сообщения
     * @param chatMessage - сообщение, которое нужно удалить
     */
    public void deleteChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.delete(chatMessage);
    }
}

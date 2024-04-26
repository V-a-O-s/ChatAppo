package ch.jchat.chatapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.jchat.chatapp.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByMessageID(Long MessageID);

    List<Message> findByChatChatID(Long chatID); 
    List<Message> findByUserUserID(Long userID);
    List<Message> findByMessageTextContaining(String messageText);
}
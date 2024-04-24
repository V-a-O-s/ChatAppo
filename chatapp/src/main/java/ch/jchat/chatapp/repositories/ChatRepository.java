package ch.jchat.chatapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.jchat.chatapp.models.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByChatID(Long ChatID);

}

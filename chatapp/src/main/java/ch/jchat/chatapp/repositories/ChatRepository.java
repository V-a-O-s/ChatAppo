package ch.jchat.chatapp.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.jchat.chatapp.models.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByChatID(Long ChatID);
    Optional<Chat> findByOwnerUserID(Long userID);
    Optional<Chat> findByLastMessage(Date lastMessage);
    List<Chat> findAll();
}


/*
    private Long chatID;
    private User owner;
    private Date creationDate;
    private Short userLimit = 2;
    private Date lastMessage;
 */
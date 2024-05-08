package ch.jchat.chatapp.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ch.jchat.chatapp.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByMessageid(Long messageid);
    List<Message> findByChatid(Long chatid);
    List<Message> findByUserid(Long userid);
    List<Message> findByMessageTextContaining(String messageText);
}

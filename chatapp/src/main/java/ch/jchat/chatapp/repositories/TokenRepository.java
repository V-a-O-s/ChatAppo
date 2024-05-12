package ch.jchat.chatapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ch.jchat.chatapp.models.auth.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.loggedOut = false")
    List<Token> findAllByUser(Long userId);

    Optional<Token> findByToken(String token);
}

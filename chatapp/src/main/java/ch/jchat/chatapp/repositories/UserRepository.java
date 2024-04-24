package ch.jchat.chatapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.jchat.chatapp.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    Optional<User> findByUserID(int UserID);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

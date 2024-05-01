package ch.jchat.chatapp.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.enums.EPlatformRoles;
import ch.jchat.chatapp.enums.EAvatar;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserID(Long userID);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByBackUpEmail(String email);
    Optional<User> findByPhone(String phone);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUserIDAndUsername(Long id, String username);

    List<User> findByRole(EPlatformRoles role);
    List<User> findByBanned(boolean banned);
    List<User> findByAvatar(EAvatar avatar);
}

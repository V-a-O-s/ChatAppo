package ch.jchat.chatapp.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.jchat.chatapp.models.Invite;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {

    Optional<Invite> findByInviteID(Long inviteID);
    Optional<Invite> findByInviteName(String inviteName);
    Optional<Invite> findByExpirationDate(Date expirationDate);
    Optional<Invite> findByActive(boolean active);
    Optional<Invite> findByChatID(Long chatID);

    boolean existsByChatID(Long chatID);
    boolean existsByInviteName(String inviteName);
    boolean existsByInviteID(Long inviteID);

    List<Invite> findAll();
}

package ch.jchat.chatapp.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.jchat.chatapp.models.Invite;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    Optional<Invite> findByInviteID(Long inviteID);
    Optional<Invite> findByInviteName(String inviteName);
    Optional<Invite> findByExpirationDate(Date expirationDate);
    Optional<Invite> findByActive(boolean active);

    List<Invite> findAll();
}

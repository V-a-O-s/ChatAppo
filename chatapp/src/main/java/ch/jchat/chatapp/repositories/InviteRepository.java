package ch.jchat.chatapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.jchat.chatapp.models.Invite;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    Optional<Invite> findByInviteID(Long InviteID);
}

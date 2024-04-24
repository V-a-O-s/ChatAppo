package ch.jchat.chatapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.jchat.chatapp.models.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByMembershipID(Long MembershipID);

}

package ch.jchat.chatapp.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.jchat.chatapp.enums.EChatRoles;
import ch.jchat.chatapp.models.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByMembershipID(Long membershipID);
    Optional<Membership> findByChatIDAndUserID(Long chatID, Long userID);

    List<Membership> findByUserID(Long userID);
    List<Membership> findByChatID(Long chatID);
    List<Membership> findByChatIDAndUserRole(Long chatID, EChatRoles userRole);
    List<Membership> findByChatIDAndJoinDate(Long chatID, LocalDateTime joinDate);
    List<Membership> findByUserIDAndBannedFalse(Long userID);

    boolean existsByChatIDAndUserID(Long chatID, Long userID);

    @Query("SELECT m FROM Membership m WHERE m.chatID = :chatId AND m.banned = true")
    List<Membership> findBannedMembersByChat(Long chatId);

    @Query("SELECT m FROM Membership m WHERE m.chatID = :chatId AND m.banned = false")
    List<Membership> findActiveMembersByChat(Long chatId);
}

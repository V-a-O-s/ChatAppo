package ch.jchat.chatapp.repositories;

import java.util.Date;
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
    Optional<Membership> findByChatChatIDAndUserUserID(Long chatID, Long userID);

    List<Membership> findByUser_UserID(Long userId);
    List<Membership> findUsersByChat_ChatID(Long chatID);
    List<Membership> findUsersByChat_ChatIDAndUserRole(Long chatID, EChatRoles userRole);
    List<Membership> findUsersByChat_ChatIDAndJoinDate(Long chatID, Date joinDate);

    boolean existsByChatChatIDAndUserUserID(Long chatID, Long userID);

    @Query("SELECT m FROM Membership m WHERE m.chat.chatID = :chatId AND m.banned = true")
    List<Membership> findBannedMembersByChatId(Long chatId);

    @Query("SELECT m FROM Membership m WHERE m.chat.chatID = :chatId AND m.banned = false")
    List<Membership> findActiveMembersByChatId(Long chatId);
}

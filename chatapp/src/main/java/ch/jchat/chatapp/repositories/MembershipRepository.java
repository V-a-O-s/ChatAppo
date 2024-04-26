package ch.jchat.chatapp.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ch.jchat.chatapp.enums.EChatRoles;
import ch.jchat.chatapp.models.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByMembershipID(Long MembershipID);

    List<Membership> findByUserUserID(Long userId);
    List<Membership> findUsersByChatChatID(Long chatID);
    List<Membership> findUsersByChatChatIDAndUserRole(Long chatID, EChatRoles userRole);
    List<Membership> findUsersByChatChatIDAndJoinDate(Long chatID, Date joinDate);

    @Query("SELECT m FROM Membership m WHERE m.chat.id = :chatId AND m.banned = true")
    Optional<List<Membership>> findBannedMembersByChatId(Long chatId);

    @Query("SELECT m FROM Membership m WHERE m.chat.id = :chatId AND m.banned = false")
    List<Membership> findActiveMembersByChatId(Long chatId);
}
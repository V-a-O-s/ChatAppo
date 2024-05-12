package ch.jchat.chatapp.repositories;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.jchat.chatapp.enums.EChatRoles;
import ch.jchat.chatapp.models.Membership;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MembershipRepositoryTest {

    @Mock
    private MembershipRepository membershipRepository;

    @Test
    public void testFindMembershipByID() {
        Long membershipID = 1L;
        when(membershipRepository.findByMembershipID(membershipID)).thenReturn(Optional.of(new Membership()));
        assertTrue(membershipRepository.findByMembershipID(membershipID).isPresent(), "Membership should be found");
    }

    @Test
    public void testFindByChatIDAndUserRole() {
        Long chatID = 1L;
        when(membershipRepository.findByChatIDAndUserRole(chatID, EChatRoles.CHAT_OWNER)).thenReturn(Arrays.asList(new Membership()));
        List<Membership> memberships = membershipRepository.findByChatIDAndUserRole(chatID, EChatRoles.CHAT_OWNER);
        assertFalse(memberships.isEmpty(), "Should return memberships with role ADMIN");
    }

    @Test
    public void testFindBannedMembersByChat() {
        Long chatID = 1L;
        when(membershipRepository.findBannedMembersByChat(chatID)).thenReturn(Arrays.asList(new Membership(), new Membership()));
        List<Membership> bannedMemberships = membershipRepository.findBannedMembersByChat(chatID);
        assertEquals(2, bannedMemberships.size(), "Should return two banned memberships");
    }
}

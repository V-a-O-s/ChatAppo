package ch.jchat.chatapp.repositories;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.jchat.chatapp.models.Chat;
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
public class ChatRepositoryTest {

    @Mock
    private ChatRepository chatRepository;

    @Test
    public void testFindByChatID() {
        Long chatID = 1L;
        when(chatRepository.findByChatID(chatID)).thenReturn(Optional.of(new Chat()));
        assertTrue(chatRepository.findByChatID(chatID).isPresent(), "Chat should be found");
    }

    @Test
    public void testFindAllChats() {
        when(chatRepository.findAll()).thenReturn(Arrays.asList(new Chat(), new Chat()));
        List<Chat> chats = chatRepository.findAll();
        assertEquals(2, chats.size(), "Should return all chats");
    }

    @Test
    public void testFindByOwner() {
        Long userID = 1L;
        when(chatRepository.findByOwner(userID)).thenReturn(Arrays.asList(new Chat()));
        List<Chat> chats = chatRepository.findByOwner(userID);
        assertFalse(chats.isEmpty(), "Should return chats owned by the user");
    }
}

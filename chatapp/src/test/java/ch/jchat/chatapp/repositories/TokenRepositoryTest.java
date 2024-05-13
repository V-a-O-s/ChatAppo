package ch.jchat.chatapp.repositories;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.jchat.chatapp.models.auth.Token;
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
public class TokenRepositoryTest {

    @Mock
    private TokenRepository tokenRepository;

    @Test
    public void testFindAllByUser() {
        Long userId = 1L;
        when(tokenRepository.findAllByUser(userId)).thenReturn(Arrays.asList(new Token(), new Token()));
        List<Token> tokens = tokenRepository.findAllByUser(userId);
        assertEquals(2, tokens.size(), "Should return two tokens");
    }

    @Test
    public void testFindByToken() {
        String tokenStr = "abc123";
        when(tokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(new Token()));
        assertTrue(tokenRepository.findByToken(tokenStr).isPresent(), "Token should be found");
    }
    
    @Test
    public void testNoTokenFound() {
        String tokenStr = "nonexistent";
        when(tokenRepository.findByToken(tokenStr)).thenReturn(Optional.empty());
        assertTrue(tokenRepository.findByToken(tokenStr).isEmpty(), "No token should be found");
    }
}

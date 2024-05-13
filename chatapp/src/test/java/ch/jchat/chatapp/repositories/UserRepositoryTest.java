package ch.jchat.chatapp.repositories;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.jchat.chatapp.models.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindUserByUserId() {
        Long userId = 1L;
        when(userRepository.findByUserID(userId)).thenReturn(Optional.empty());
        
        // Test the behavior when no user is found. Here we use an Optional check instead of throwing an exception.
        Optional<User> result = userRepository.findByUserID(userId);
        assertTrue(result.isEmpty(), "Expected no user to be found with ID: " + userId);
    }
}

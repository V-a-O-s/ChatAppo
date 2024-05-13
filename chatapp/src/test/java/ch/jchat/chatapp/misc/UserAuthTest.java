package ch.jchat.chatapp.misc;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserAuthTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserAuth userAuth;

    @Test
    public void testGetUser_UserFoundAndVerified() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user1");

        User user = new User();
        user.setVerified(true);
        when(userRepository.findByUsername("user1")).thenReturn(java.util.Optional.of(user));
        assertEquals(user, userAuth.getUser(), "Should return the verified user");
    }

    @Test
    public void testGetUser_UserFoundButNotVerified() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user2");

        User user = new User();
        user.setVerified(false);
        when(userRepository.findByUsername("user2")).thenReturn(java.util.Optional.of(user));

        Exception exception = assertThrows(RuntimeException.class, () -> userAuth.getUser());
        assertEquals("User not verified", exception.getMessage());
    }

    @Test
    public void testGetUser_UserNotFound() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("unknown");

        when(userRepository.findByUsername("unknown")).thenReturn(java.util.Optional.empty());
        
        Exception exception = assertThrows(RuntimeException.class, () -> userAuth.getUser());
        assertEquals("User not found", exception.getMessage());
    }
}

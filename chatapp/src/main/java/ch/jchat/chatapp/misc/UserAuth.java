package ch.jchat.chatapp.misc;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.UserRepository;

@Service
public class UserAuth {

    @Autowired
    private UserRepository userRepository;
    
    public User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

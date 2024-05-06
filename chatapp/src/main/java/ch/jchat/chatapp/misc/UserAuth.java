package ch.jchat.chatapp.misc;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAuth {

    @Autowired
    private UserRepository userRepository;
    
    public User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("\n\n"+username+"\n"+SecurityContextHolder.getContext().getAuthentication()+"\n"+SecurityContextHolder.getContext());
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (u.isVerified()) {
            return u;
        }
        throw new RuntimeException("User not verified");
    }
}

package ch.jchat.chatapp.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username: {}", username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (user.isBanned()) {
            log.warn("Banned user attempted to login: {}", username);
            throw new UsernameNotFoundException("User is banned: " + username);
        }

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        log.info("User loaded successfully: {}", username);
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPassword(), 
            Collections.singletonList(authority)
        );
    }
}

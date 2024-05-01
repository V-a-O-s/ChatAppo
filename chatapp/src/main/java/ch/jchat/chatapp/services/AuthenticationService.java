package ch.jchat.chatapp.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import ch.jchat.chatapp.enums.EAvatar;
import ch.jchat.chatapp.enums.EPlatformRoles;
import ch.jchat.chatapp.exceptions.UserAlreadyExists;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.models.auth.AuthenticationResponse;
import ch.jchat.chatapp.models.auth.Token;
import ch.jchat.chatapp.repositories.TokenRepository;
import ch.jchat.chatapp.repositories.UserRepository;
import ch.jchat.chatapp.security.jwt.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(User request) {
        
        userRepository.findByUsername(request.getUsername()).ifPresent(u -> {
            throw new UserAlreadyExists("User with username " + request.getUsername() + " already exists.");
        });

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setBackUpEmail(request.getBackUpEmail());
        newUser.setPhone(request.getPhone());
        newUser.setCreationDate(new Date());
        newUser.setRole(EPlatformRoles.USER);
        newUser.setAvatar(EAvatar.GREEN);
        newUser.setEnabled(true);

        newUser = userRepository.save(newUser);
        String token = jwtService.generateToken(newUser);
        
        saveUserToken(newUser, token);

        return new AuthenticationResponse(token);

    }
    
    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), 
                request.getPassword()
            )
        );

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getUsername()));
    
        String token = jwtService.generateToken(user);
        saveUserToken(user, token);
        return new AuthenticationResponse(token);
    }

    private void saveUserToken(User newUser, String jwtToken) {

        List<Token> validTokenListByUser = tokenRepository.findAllByUser(newUser.getUserID());
        if (!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(t->{
                t.setLoggedOut(true);
            });
        }

        Token token = new Token();
        token.setToken(jwtToken);
        token.setLoggedOut(false);
        token.setUser(newUser);
        tokenRepository.save(token);
    }
}
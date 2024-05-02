package ch.jchat.chatapp.services;

import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.jchat.chatapp.enums.EAvatar;
import ch.jchat.chatapp.enums.EPlatformRoles;
import ch.jchat.chatapp.exceptions.EmailNotFoundException;
import ch.jchat.chatapp.exceptions.UserAlreadyExists;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.models.auth.AuthenticationResponse;
import ch.jchat.chatapp.models.auth.Token;
import ch.jchat.chatapp.repositories.TokenRepository;
import ch.jchat.chatapp.repositories.UserRepository;
import ch.jchat.chatapp.security.jwt.JwtService;
import lombok.AllArgsConstructor;

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
            throw new UserAlreadyExists("User with the username " + request.getUsername() + " already exists.");
        });

        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExists("User with the email " + request.getEmail() + " already exists.");
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
                userRepository.findByEmail(request.getEmail()).get().getUsername(),
                request.getPassword()
            )
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new EmailNotFoundException("User not found with email: " + request.getEmail()));
    
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
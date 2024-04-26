package ch.jchat.chatapp.controller;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.jchat.chatapp.enums.EAvatar;
import ch.jchat.chatapp.enums.EPlatformRoles;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.UserRepository;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AccountController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            //System.out.println(userRepository.findByUsername(userDto.getUsername()).get().getSalt());
            return ResponseEntity.badRequest().body("Username already exists.");
        }

        User newUser = new User();
        String salt = generateSalt();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword() + salt));
        newUser.setSalt(salt);
        newUser.setEmail(userDto.getEmail());
        newUser.setBackUpEmail(userDto.getBackUpEmail());
        newUser.setPhone(userDto.getPhone());
        newUser.setCreationDate(new Date());
        newUser.setRole(EPlatformRoles.USER);
        newUser.setAvatar(userDto.getAvatar() == null ? EAvatar.GREEN : userDto.getAvatar());
    
        System.out.println("\n\n\n\n"+newUser.toString()+"\n\n\n\n");

        userRepository.save(newUser);
        return ResponseEntity.ok("User created\nWelcome to JChat "+newUser.getUsername()+"!");
        // ResponseEntity.ok("User registered successfully");
        
    }

    @PostMapping("/login")
    public ResponseEntity<String> signIn(@RequestBody User userDto){
        
        if (userRepository.existsByUsername(userDto.getUsername()) || userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.ok("logged in wirh username/email");
        }

        

        return ResponseEntity.badRequest().body("No User found with these Credentials");
    }

    private String generateSalt(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }    
}

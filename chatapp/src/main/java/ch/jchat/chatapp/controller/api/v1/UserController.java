package ch.jchat.chatapp.controller.api.v1;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.jchat.chatapp.enums.EAvatar;
import ch.jchat.chatapp.misc.Validator;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.UserRepository;

import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public ResponseEntity<String> getPublicResp(){
        return ResponseEntity.ok("Hello");
    }

    @PutMapping("username")
    public ResponseEntity<String> changUsername(@RequestBody List<String> user){
        if (userRepository.existsByUsername(user.get(0))) {
            userRepository.findByUsername(user.get(0)).get().setUsername(user.get(1));
            return ResponseEntity.ok("Username changed successfully");
        }

        return ResponseEntity.badRequest().body("Username change not possible");
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody List<String> userList){
        Optional<User> user = userRepository.findByUsername(userList.get(0));
        if (user.isPresent() && passwordEncoder.matches(userList.get(1), user.get().getPassword())) {
            
            user.get().setPassword(passwordEncoder.encode(userList.get(2)));
            return ResponseEntity.ok("Password changed successfully");
        }

        return ResponseEntity.badRequest().body("Password change not possible");
    }

    @PutMapping("/email")
    public ResponseEntity<String> changeEmail(@RequestBody List<String> userList){
        Optional<User> user = userRepository.findByEmail(userList.get(0));
        if (user.isPresent() && Validator.isEmail(userList.get(0))) {
            
            user.get().setEmail(userList.get(1));
            return ResponseEntity.ok("Email changed successfully");
        }

        return ResponseEntity.badRequest().body("Email change not possible");
    }
    @PutMapping("/backupemail")
    public ResponseEntity<String> changeBackupEmail(@RequestBody List<String> userList){
        Optional<User> user = userRepository.findByBackUpEmail(userList.get(0));
        if (user.isPresent() && Validator.isEmail(userList.get(1))) {
            
            user.get().setBackUpEmail(userList.get(1));
            return ResponseEntity.ok("Backup-Email changed successfully");
        }

        return ResponseEntity.badRequest().body("Backup-Email change not possible");
    }

    @PutMapping("/phone")
    public ResponseEntity<String> changePhone(@RequestBody List<String> userList){
        Optional<User> user = userRepository.findByEmail(userList.get(0));
        if (user.isPresent() && Validator.isPhone(userList.get(1))) {
            
            user.get().setBackUpEmail(userList.get(1));
            return ResponseEntity.ok("Backup-Email changed successfully");
        }

        return ResponseEntity.badRequest().body("Backup-Email change not possible");
    }


    @PutMapping("/avatar")
    public ResponseEntity<String> changeAvatar(@RequestBody List<String> userList){
        Optional<User> user = userRepository.findByUsername(userList.get(0));
        if (user.isPresent()) {
            
            try {
                user.get().setAvatar(EAvatar.valueOf(userList.get(1)));
                return ResponseEntity.ok("Avatar changed successfully");
            } catch (Exception e) {
                return ResponseEntity.ok("Avatar not available");
            }    
        }
        return ResponseEntity.badRequest().body("Avatar change not possible. User not found");
    }

    /*
     * (c change (multi) / s set (single))
     * Username c -
     * Password c -
     * Email c - 
     * Avatar (default GREEN) c -
     * 
     * create s
     * modify c 
     * delete s
     * 
     */
}

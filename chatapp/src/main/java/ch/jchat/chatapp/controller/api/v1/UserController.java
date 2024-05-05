package ch.jchat.chatapp.controller.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.jchat.chatapp.misc.UserAuth;
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
    @Autowired
    private UserAuth userAuth;

    @PostMapping("/username")
    public ResponseEntity<String> changUsername(@RequestBody String user){
        User currentUser = userAuth.getUser();
        if (Validator.isName(user) && userRepository.findByUsername(user).isEmpty()) {
            currentUser.setUsername(user);
            userRepository.save(currentUser);
            return ResponseEntity.ok("Username changed successfully");
        }
        return new ResponseEntity<>("Invalid Username. Or it is already choosen.", HttpStatus.CONFLICT);
        
    }
    @PostMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody String oldPassword, String newPassword){
        User currentUser = userAuth.getUser();
        if (passwordEncoder.matches(oldPassword, currentUser.getPassword())){
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(currentUser);
            return ResponseEntity.ok("Password changed successfully");
        }
        return ResponseEntity.badRequest().body("Something went wrong");
    }
    @PostMapping("/email")
    public ResponseEntity<String> changeEmail(@RequestBody String email){
        User currentUser = userAuth.getUser(); //Made By Valentin Ostertag
        if (Validator.isEmail(email) && userRepository.findByEmail(email).isEmpty()) {
            currentUser.setEmail(email);
            userRepository.save(currentUser);
            return ResponseEntity.ok("Email changed successfully");
        }
        return ResponseEntity.badRequest().body("Email change not possible");
    }
    @PostMapping("/backupemail")
    public ResponseEntity<String> changeBackupEmail(@RequestBody String email){
        User currentUser = userAuth.getUser();
        if (Validator.isEmail(email)) {
            currentUser.setBackUpEmail(email);
            userRepository.save(currentUser);
            return ResponseEntity.ok("Backup-Email changed successfully");
        }
        return ResponseEntity.badRequest().body("Backup-Email change not possible");
    }
    @PostMapping("/phone")
    public ResponseEntity<String> changePhone(@RequestBody String phone){
        User currentUser = userAuth.getUser();
        phone = phone.replaceAll("\\s", "");
        if (Validator.isPhone(phone) && userRepository.findByPhone(phone).isEmpty()) {
            currentUser.setPhone(phone);
            userRepository.save(currentUser);
            return ResponseEntity.ok("Phone changed successfully");
        }
        return ResponseEntity.badRequest().body("Phone change not possible");
    }
    @PostMapping("/avatar")
    public ResponseEntity<String> changeAvatar(@RequestBody User user){
        User currentUser = userAuth.getUser();
        try{
            currentUser.setAvatar(user.getAvatar());
            userRepository.save(currentUser);
            return ResponseEntity.ok("Avatar changed successfully");
        } catch (Exception e) {
            return ResponseEntity.ok("Avatar not available");
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody String password){
        User currentUser = userAuth.getUser();
        if (passwordEncoder.matches(password, currentUser.getPassword())) {
            return ResponseEntity.ok("Account Deleted.");
        }
        return ResponseEntity.badRequest().body("Something went wrong. Try again later.");
    }
}
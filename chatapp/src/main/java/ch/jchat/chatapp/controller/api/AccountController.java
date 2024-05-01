package ch.jchat.chatapp.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.jchat.chatapp.enums.EPlatformRoles;
import ch.jchat.chatapp.misc.AppConfig;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.models.auth.AuthenticationResponse;
import ch.jchat.chatapp.repositories.UserRepository;
import ch.jchat.chatapp.services.AuthenticationService;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AccountController {

    
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final AppConfig appConfig;

    @GetMapping("/key")
    public String key(){
        return appConfig.getJwtSecret();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.authenticate(user));
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody String email){

        if(userRepository.existsByEmail(email)){
            return ResponseEntity.ok("User Verified.");
        }
        return new ResponseEntity<>("Verification not possible",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> userLogout(@RequestBody User user){
        return ResponseEntity.ok("Bye!");
    }

    @PostMapping("/upgrade")
    public ResponseEntity<?> upgradeAccount(@RequestBody List<String> upgrade){
        try {
            User usr = userRepository.findByUsername(upgrade.get(1)).get();
            usr.setRole(EPlatformRoles.VIP);
            return ResponseEntity.ok("Rank: "+upgrade.get(0)+"\nGiven to "+upgrade.get(1));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not found");
        }
        
    }

    @PostMapping("/s/ban")
    public ResponseEntity<String> bannUser(@RequestBody User user){
        if (isValid(userRepository, user)) {
            userRepository.delete(user);
            return ResponseEntity.ok(user.getUsername()+" was deleted");
        }
        return ResponseEntity.badRequest().body("Not able to ban this user");
        
        
        

    }

    private boolean isValid(UserRepository userRepository,User user){
        
        try {
            userRepository.existsById(user.getUserID());
        } catch (Exception e) {
            try {
                userRepository.existsByUsername(user.getUsername());
            } catch (Exception ee) {
                try {
                    userRepository.existsById(user.getUserID());
                } catch (Exception eee) {
                    return false;
                }
            }
        }
        return true;
    }

}

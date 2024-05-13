package ch.jchat.chatapp.controller.api;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.jchat.chatapp.enums.EPlatformRoles;
import ch.jchat.chatapp.misc.UserAuth;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.models.auth.AuthenticationResponse;
import ch.jchat.chatapp.models.dto.AuthDto;
import ch.jchat.chatapp.models.dto.VerifyDto;
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
    private final UserAuth userAuth;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody AuthDto user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthDto user) {
        return ResponseEntity.ok(authenticationService.authenticate(user));
    }

    @PostMapping("/verify")
    @Transactional
    public ResponseEntity<String> verifyUser(@RequestBody VerifyDto authUser){
        //User currentUser = userAuth.getUser();
        Optional<User> user = userRepository.findByEmail(authUser.getEmail().replaceAll("\"", ""));
        if (!user.isPresent() || user.get().isVerified()) {
            return new ResponseEntity<>("User already Verified or User doesn't exist.",HttpStatus.CONFLICT);
        } else {
            if (passwordEncoder.matches(authUser.getPassword(), user.get().getPassword())) {
                user.get().setVerified(true);
                userRepository.save(user.get());
                return ResponseEntity.ok("User Verified.");
            }
            return new ResponseEntity<>("Password does not match.",HttpStatus.BAD_REQUEST);
        }
        
    }

    /*
    @PostMapping("/upgrade")
    @Transactional
    public ResponseEntity<?> upgradeAccount(@RequestBody List<String> upgrade){
        try {
            User usr = userRepository.findByUsername(upgrade.get(1)).get();
            usr.setRole(EPlatformRoles.VIP);
            return ResponseEntity.ok("Rank: "+upgrade.get(0)+"\nGiven to "+upgrade.get(1));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not found");
        }
        
    }
    //*/

    @PostMapping("/s/ban")
    @Transactional
    public ResponseEntity<String> bannUser(@RequestBody AuthDto user){
        User admin = userAuth.getUser();
        if (userRepository.existsById(user.getUserID()) && admin.getRole()==EPlatformRoles.ADMIN) {
            userRepository.findById(user.getUserID()).get().setBanned(true);

            return ResponseEntity.ok(user.getUsername()+" was deleted");
        }
        return ResponseEntity.badRequest().body("Not able to ban this user");
    }
}
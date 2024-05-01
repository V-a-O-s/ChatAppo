package ch.jchat.chatapp.controller.api.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/member")
public class MembershipController {

    @GetMapping("/hello")
    public ResponseEntity<String> getPublicResp(){
        return ResponseEntity.ok("Hello");
    }

    /*
     * (c change (multi) / s set (single) / d delete)
     * MembershipID s
     * ChatID s
     * UserID s
     * UserRole c
     * JoinDate s
     * ChatTimeout c
     * Banned c
     * BanDate c
     * BanReason c
     * 
     * create s
     * modify c
     * delete s
     */
    
}

package ch.jchat.chatapp.controller.api.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/invite")
public class InviteController {
    
    @GetMapping("/hello")
    public ResponseEntity<String> getPublicResp(){
        return ResponseEntity.ok("Hello");
    }
    
    /*
     * (c change (multi) / s set (single) / d delete)
     * InviteID sd
     * ChatID s
     * InvitedByUserID sd
     * InviteName c
     * ExpirationDate c
     * Active c
     * 
     * create s
     * modify c
     * delete s
    */
}

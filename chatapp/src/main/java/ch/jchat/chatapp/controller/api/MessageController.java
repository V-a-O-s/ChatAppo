package ch.jchat.chatapp.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/message")
public class MessageController {

    @GetMapping("/hello")
    public ResponseEntity<String> getPublicResp(){
        return ResponseEntity.ok("Hello");
    }

    /*
     * (c change (multi) / s set (single))
     * MessageID s
     * ChatID s
     * UserID s
     * MessageText c
     * SendingTime s
     * 
     * Send s
     * Modify c
     * Delete s
    */
}

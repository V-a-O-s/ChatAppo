package ch.jchat.chatapp.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/hello")
    public ResponseEntity<String> getPublicResp(){
        return ResponseEntity.ok("Hello");
    }

    /*
     * (c change (multi) / s set (single))
     * UserID s
     * Username c
     * Password c
     * Salt s
     * Email c 
     * Role c
     * Avatar (default GREEN) c
     * Banned c
     * CreationDate s
     * 
     * create s
     * modify c 
     * delete s
     * 
     */
}

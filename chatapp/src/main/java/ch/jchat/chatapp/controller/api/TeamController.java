package ch.jchat.chatapp.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/team")
public class TeamController {
    @GetMapping("/admin")
    public ResponseEntity<String> testAdmin(){
        return ResponseEntity.ok("Alles ok");
    }
    @GetMapping("/support")
    public ResponseEntity<String> testSupport(){
        return ResponseEntity.ok("Support läuft");
    }
}

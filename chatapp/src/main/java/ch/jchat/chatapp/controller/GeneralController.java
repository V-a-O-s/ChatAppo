package ch.jchat.chatapp.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class GeneralController {
    @GetMapping("/")
    public String test(){
        return "Hello from the Backend";
    }
}

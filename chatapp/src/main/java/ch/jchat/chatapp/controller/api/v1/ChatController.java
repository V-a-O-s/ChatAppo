package ch.jchat.chatapp.controller.api.v1;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.jchat.chatapp.models.Chat;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.ChatRepository;
import ch.jchat.chatapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/chat")
@AllArgsConstructor
public class ChatController {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public ResponseEntity<String> getPublicResp(){
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/create")
    public ResponseEntity<String> createChat(@RequestBody Chat chat){

        String OwnerName = reqUser().getUsername();

        log.debug("\n\n Username is "+OwnerName);


        if (!userRepository.existsByUsername(OwnerName)) {
            return ResponseEntity.badRequest().body("Error while creating Chat with "+OwnerName+" as the Owner");
        }

        Chat newChat = new Chat();
        newChat.setOwner(userRepository.findByUsername(OwnerName).get());
        newChat.setCreationDate(new Date());
        newChat.setUserLimit((chat.getUserLimit()<255)?chat.getUserLimit():10);
        newChat.setLastActivity(new Date());
        newChat.setChatName(chat.getChatName());

        chatRepository.save(newChat);

        return ResponseEntity.ok("Chat "+chat.getChatName()+", was created.");
    }

    @PutMapping("/limit")
    public ResponseEntity<String> changeUserLimit(@RequestBody Chat chat){

        Chat target = chatRepository.findByChatID(chat.getChatID()).orElseThrow();

        log.debug("\n"+target.toString());

        if (target.getOwner().getUserID()==reqUser().getUserID()) {
            target.setUserLimit(chat.getUserLimit());
            chatRepository.save(target);
            return ResponseEntity.ok("Userlimit set to "+chat.getUserLimit());
        }
        return ResponseEntity.badRequest().body("Not Authorized to change the Userlimit");
    }

    @PutMapping("/name")
    public ResponseEntity<String> changeChatName(@RequestBody Chat chat){

        Chat target = chatRepository.findByChatID(chat.getChatID()).orElseThrow();

        log.debug("\n"+target.toString());

        if (target.getOwner().getUserID()==reqUser().getUserID()) {
            target.setChatName(chat.getChatName());
            chatRepository.save(target);
            return ResponseEntity.ok("Chatname set to "+chat.getChatName());
        }
        return ResponseEntity.badRequest().body("Not Authorized to change the Chatname");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteChat(@RequestBody Chat chat){
        if (reqUser().getUserID()==chat.getOwner().getUserID() 
        && passwordEncoder.matches(chat.getOwner().getPassword(), 
                                    userRepository.findByUserID(reqUser().getUserID()).get().getPassword())) {
            chatRepository.delete(chat);
            return new ResponseEntity<>(chat.getChatName()+" was deleted.",HttpStatus.OK);
        }

        return new ResponseEntity<>("You are not Authorized to delete this Chat!",HttpStatus.UNAUTHORIZED);
    }

    public User reqUser(){
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
    }
    /*
     * (c change (multi) / s set (single))
     * create s
     * modify c
     * delete s
     */
}

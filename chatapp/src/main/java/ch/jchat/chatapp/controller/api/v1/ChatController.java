package ch.jchat.chatapp.controller.api.v1;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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

import ch.jchat.chatapp.misc.UserAuth;
import ch.jchat.chatapp.models.Chat;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.ChatRepository;
import ch.jchat.chatapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserAuth userAuth; 
    @PostMapping("/create")
    public ResponseEntity<String> createChat(@RequestBody Chat chat){
        User currentUser = userAuth.getUser();
        String OwnerName = currentUser.getUsername();
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
        log.debug("Created Chat: "+newChat.toString());
        return ResponseEntity.ok("Chat "+chat.getChatName()+", was created.");
    }
    @PostMapping("/limit")
    public ResponseEntity<String> changeUserLimit(@RequestBody Chat chat){
        User currentUser = userAuth.getUser();
        Chat target = chatRepository.findByChatID(chat.getChatID()).orElseThrow();
        log.debug("Setting ChannelLimit to "+chat.getUserLimit());
        if (target.getOwner().getUserID()==currentUser.getUserID()) {
            target.setUserLimit(chat.getUserLimit());
            chatRepository.save(target);
            return ResponseEntity.ok("Userlimit set to "+chat.getUserLimit());
        }
        return ResponseEntity.badRequest().body("Not Authorized to change the Userlimit");
    }
    @PostMapping("/name")
    public ResponseEntity<String> changeChatName(@RequestBody Chat chat){
        User currentUser = userAuth.getUser();
        Chat target = chatRepository.findByChatID(chat.getChatID()).orElseThrow();
        log.debug("\n"+target.toString());
        if (target.getOwner().getUserID()==currentUser.getUserID()) {
            target.setChatName(chat.getChatName());
            chatRepository.save(target);
            return ResponseEntity.ok("Chatname set to "+chat.getChatName());
        }
        return ResponseEntity.badRequest().body("Not Authorized to change the Chatname");
    }
    @PostMapping("/delete")
    public ResponseEntity<String> deleteChat(@RequestBody Chat chat){
        User currentUser = userAuth.getUser();
        if (currentUser.getUserID()==chat.getOwner().getUserID() 
        && passwordEncoder.matches(chat.getOwner().getPassword(), 
                                    userRepository.findByUserID(currentUser.getUserID()).get().getPassword())) {
            chatRepository.delete(chat);
            return new ResponseEntity<>(chat.getChatName()+" was deleted.",HttpStatus.OK);
        }
        return new ResponseEntity<>("You are not Authorized to delete this Chat!",HttpStatus.UNAUTHORIZED);
    }
}
package ch.jchat.chatapp.controller.api.v1;

import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.jchat.chatapp.enums.EChatRoles;
import ch.jchat.chatapp.misc.UserAuth;
import ch.jchat.chatapp.models.Membership;
import ch.jchat.chatapp.models.Message;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.ChatRepository;
import ch.jchat.chatapp.repositories.MembershipRepository;
import ch.jchat.chatapp.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/message")
@AllArgsConstructor
public class MessageController {

    
    private final UserAuth userAuth;
    private final ChatRepository chatRepository;
    private final MembershipRepository membershipRepository;
    private final MessageRepository messageRepository;

    @PostMapping("/send")
public ResponseEntity<?> sendMessage(@RequestBody Message msg){
    User currentUser = userAuth.getUser(); 
    Optional<Membership> member = membershipRepository.findByChatIDAndUserID(msg.getChatid(), currentUser.getUserID());
    if (!currentUser.getUserID().equals(member.get().getUserID())) { // Adjust here based on Membership structure
        return new ResponseEntity<>("Unauthorized action.", HttpStatus.UNAUTHORIZED);
    }
    Message newMsg = new Message();
    log.debug(msg.getMessageText().length() + " - " + msg.getMessageText());

    newMsg.setChatid(chatRepository.findByChatID(msg.getChatid()).orElseThrow().getChatID());
    newMsg.setMessageText((msg.getMessageText().length() <= 2000 && !msg.getMessageText().isEmpty()) ? msg.getMessageText() : "This user tried to send an invalid Message. Boo him B)");
    newMsg.setSendingTime(new Date());
    newMsg.setUserid(currentUser.getUserID());
    messageRepository.save(newMsg);
    return new ResponseEntity<>("Message sent", HttpStatus.OK);
}

    @PostMapping("/recive/{chatID}")
    public ResponseEntity<?> getAllMessages(@PathVariable Long chatID){
        User currentUser = userAuth.getUser();
        Membership member = membershipRepository.findByChatIDAndUserID(chatID, currentUser.getUserID()).orElseThrow(() -> {
            throw new IllegalArgumentException("You are not a Member of this chat.");
        });
        
        if (member.getUserRole()==EChatRoles.CHAT_LOCKED || member.isBanned()) {
            return new ResponseEntity<>("Not autorized to see the Messages in this Chat",HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(messageRepository.findByChatid(chatID));
    }
    /*
     * (c change (multi) / s set (single))
     * MessageID s
     * ChatID s
     * UserID s
     * MessageText s
     * SendingTime s
     * 
     * Send s
     * Modify c
     * Delete s
    */
}

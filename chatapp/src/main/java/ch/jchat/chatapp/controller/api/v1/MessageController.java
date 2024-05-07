package ch.jchat.chatapp.controller.api.v1;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import ch.jchat.chatapp.models.dto.UserDto;
import ch.jchat.chatapp.repositories.ChatRepository;
import ch.jchat.chatapp.repositories.MembershipRepository;
import ch.jchat.chatapp.repositories.MessageRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    @Autowired
    private UserAuth userAuth;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserDto userDto;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Message msg){
        User currentUser = userAuth.getUser(); 
        Optional<Membership> member = membershipRepository.findByChatChatIDAndUserUserID(msg.getChat().getChatID(), currentUser.getUserID());
        if (currentUser.getUserID()==member.get().getUser().getUserID()) {
            
        }
        Message newMsg = new Message();
        log.debug(msg.getMessageText().length()+" - "+msg.getMessageText());
        


        newMsg.setChat(chatRepository.findByChatID(msg.getChat().getChatID()).orElseThrow());
        newMsg.setMessageText((msg.getMessageText().length()<=2000 && !msg.getMessageText().isEmpty())?msg.getMessageText():"This user tried to send a invalid Message. Boo him B)");
        newMsg.setSendingTime(new Date());
        newMsg.setUser(userDto.toDto(currentUser));
        messageRepository.save(newMsg);
        return new ResponseEntity<>("Message send ",HttpStatus.OK) ;
    }
    @PostMapping("/recive/{chatID}")
    public ResponseEntity<?> getAllMessages(@PathVariable Long chatID){
        User currentUser = userAuth.getUser();
        Membership member = membershipRepository.findByChatChatIDAndUserUserID(chatID, currentUser.getUserID()).orElseThrow(() -> {
            throw new IllegalArgumentException("You are not a Member of this chat.");
        });
        
        if (member.getUserRole()==EChatRoles.CHAT_LOCKED || member.isBanned()) {
            return new ResponseEntity<>("Not autorized to see the Messages in this Chat",HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(messageRepository.findByChatChatID(chatID));
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

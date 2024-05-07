package ch.jchat.chatapp.controller.api.v1;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.jchat.chatapp.enums.EChatRoles;
import ch.jchat.chatapp.misc.UserAuth;
import ch.jchat.chatapp.models.Chat;
import ch.jchat.chatapp.models.Membership;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.models.dto.UserDto;
import ch.jchat.chatapp.repositories.ChatRepository;
import ch.jchat.chatapp.repositories.MembershipRepository;
import ch.jchat.chatapp.repositories.UserRepository;

@RestController
@RequestMapping("/api/v1/member")
public class MembershipController {

    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserAuth userAuth;
    @Autowired
    private UserDto userDto;

    @PostMapping("/join/{chatID}")
    public ResponseEntity<?> joinChat(@PathVariable Long chatID) {
        User currentUser = userAuth.getUser();
        if (membershipRepository.existsByChatChatIDAndUserUserID(chatID, currentUser.getUserID())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already a member of the chat.");
        }
        if (!chatRepository.existsById(chatID)) {
            return ResponseEntity.notFound().build();
        }
        EChatRoles role;
        if (chatRepository.findById(chatID).get().getOwner().getUserID() == currentUser.getUserID()) {
            role = EChatRoles.CHAT_OWNER;
        } else {
            role = EChatRoles.CHAT_USER;
        }
        
        Membership newMembership = new Membership();
        newMembership.setChat(chatRepository.findById(chatID).get());
        newMembership.setUser(currentUser);
        newMembership.setJoinDate(LocalDateTime.now());
        newMembership.setUserRole(role);
        membershipRepository.save(newMembership);
        return ResponseEntity
                .ok("Successfully joined the chat: " + chatRepository.findByChatID(chatID).get().getChatName());
    }
    @PostMapping("/leave/{chatId}")
    public ResponseEntity<String> leaveChat(@PathVariable Long chatId) {
        User currentUser = userAuth.getUser();
        Membership membership = membershipRepository.findByChatChatIDAndUserUserID(chatId, currentUser.getUserID())
                .orElseThrow(() -> new IllegalStateException("You are not a member of this chat"));
        membershipRepository.delete(membership);
        return ResponseEntity.ok("You have successfully left the chat.");
    }
    @PostMapping("/setRole")
    public ResponseEntity<String> changeUserRole(@RequestBody Membership member) {
        User currentUser = userAuth.getUser();
        Chat chat = member.getChat();
        User target = userRepository.findByUserID(member.getUser().getUserID()).orElseThrow();

        Membership targetMembership = membershipRepository.findByChatChatIDAndUserUserID(chat.getChatID(), target.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("Target user membership not found"));
        if (!canChangeRole(membershipRepository.findByChatChatIDAndUserUserID(chat.getChatID(), currentUser.getUserID()).get(), member.getUserRole())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized role change attempt.");
        } //Made By Valentin Ostertag
        targetMembership.setUserRole(member.getUserRole());
        membershipRepository.save(targetMembership);
        return ResponseEntity.ok("The role from the User: '"+target.getUsername()+"' updated successfully.");
    }
    @PostMapping("/ban")
    public ResponseEntity<String> banUser(@RequestBody Membership target) {
        User currentUser = userAuth.getUser();
        if (!canChangeRole(membershipRepository.findByChatChatIDAndUserUserID(target.getChat().getChatID(), currentUser.getUserID()).get(),
                membershipRepository.findByChatChatIDAndUserUserID(target.getChat().getChatID(), 
                target.getUser().getUserID()).orElseThrow().getUserRole())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Action.");
        }
        Membership targetMembership = membershipRepository.findByChatChatIDAndUserUserID(target.getChat().getChatID(), target.getUser().getUserID())
                .orElseThrow();
        if (!canChangeRole(membershipRepository.findByChatChatIDAndUserUserID(target.getChat().getChatID(), currentUser.getUserID()).get(),targetMembership.getUserRole())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Action.");
        }
        String banReason;
        if(target.getBanReason().isEmpty() || target.getBanReason().length()>255){
            Date d = new Date();
            banReason = "User was banned by "+currentUser.getUsername()+" on "+d;
        }else{
            banReason = target.getBanReason();
        }
        targetMembership.setUserRole(EChatRoles.CHAT_LOCKED);
        targetMembership.setBanDate(new Date());
        targetMembership.setBanned(true);
        targetMembership.setBanReason(banReason);
        membershipRepository.save(targetMembership);
        return ResponseEntity.ok(userRepository.findById(target.getUser().getUserID()).get().getUsername()+" was Banned.");
    }
    private boolean canChangeRole(Membership changer, EChatRoles targetRole) {
        switch (changer.getUserRole()) {
            case CHAT_OWNER:
                return true;
            case CHAT_MODERATOR:
                return targetRole == EChatRoles.CHAT_USER || targetRole == EChatRoles.CHAT_LOCKED;
            default:
                return false;
        }
    }
}
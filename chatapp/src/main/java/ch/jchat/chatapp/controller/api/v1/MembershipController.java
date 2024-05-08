package ch.jchat.chatapp.controller.api.v1;

import java.time.LocalDateTime;
import java.util.Date;

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
import ch.jchat.chatapp.repositories.ChatRepository;
import ch.jchat.chatapp.repositories.MembershipRepository;
import ch.jchat.chatapp.repositories.UserRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/member")
@AllArgsConstructor
public class MembershipController {

    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final UserAuth userAuth;

    @PostMapping("/join/{chatID}")
    public ResponseEntity<String> joinChat(@PathVariable Long chatID) {
        User currentUser = userAuth.getUser();
        if (membershipRepository.existsByChatIDAndUserID(chatID, currentUser.getUserID())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already a member of the chat.");
        }
        Chat chat = chatRepository.findById(chatID).orElseThrow(() -> new IllegalArgumentException("Chat does not exist."));
        EChatRoles role = (chat.getOwner().equals(currentUser.getUserID())) ? EChatRoles.CHAT_OWNER : EChatRoles.CHAT_USER;

        Membership newMembership = new Membership();
        newMembership.setChatID(chat.getChatID());
        newMembership.setUserID(currentUser.getUserID());
        newMembership.setJoinDate(LocalDateTime.now());
        newMembership.setUserRole(role);
        membershipRepository.save(newMembership);
        return ResponseEntity.ok("Successfully joined the chat: " + chat.getChatName());
    }

    @PostMapping("/leave/{chatId}")
    public ResponseEntity<String> leaveChat(@PathVariable Long chatId) {
        User currentUser = userAuth.getUser();
        Membership membership = membershipRepository.findByChatIDAndUserID(chatId, currentUser.getUserID())
                .orElseThrow(() -> new IllegalStateException("You are not a member of this chat"));
        membershipRepository.delete(membership);
        return ResponseEntity.ok("You have successfully left the chat.");
    }
    @PostMapping("/setRole")
    public ResponseEntity<String> changeUserRole(@RequestBody Membership member) {
        User currentUser = userAuth.getUser();
        Chat chat = chatRepository.findByChatID(member.getChatID()).orElseThrow();
        //User target = userRepository.findByUserID(member.getUser()).orElseThrow();
        User target = userRepository.findByUserID(member.getUserID()).orElseThrow();

        Membership targetMembership = membershipRepository.findByChatIDAndUserID(chat.getChatID(), target.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("Target user membership not found"));
        if (!canChangeRole(membershipRepository.findByChatIDAndUserID(chat.getChatID(), currentUser.getUserID()).orElseThrow(), member.getUserRole())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized role change attempt.");
        } //Made By Valentin Ostertag
        targetMembership.setUserRole(member.getUserRole());
        membershipRepository.save(targetMembership);
        return ResponseEntity.ok("The role from the User: '"+target.getUsername()+"' updated successfully.");
    }
    @PostMapping("/ban")
    public ResponseEntity<String> banUser(@RequestBody Membership target) {
        User currentUser = userAuth.getUser();
        if (!canChangeRole(membershipRepository.findByChatIDAndUserID(target.getChatID(), currentUser.getUserID()).orElseThrow(),
                membershipRepository.findByChatIDAndUserID(target.getChatID(), 
                target.getUserID()).orElseThrow().getUserRole())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Action.");
        }
        Membership targetMembership = membershipRepository.findByChatIDAndUserID(target.getChatID(), target.getUserID())
                .orElseThrow();
        if (!canChangeRole(membershipRepository.findByChatIDAndUserID(target.getChatID(), currentUser.getUserID()).orElseThrow(),targetMembership.getUserRole())) {
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
        return ResponseEntity.ok(userRepository.findByUserID(target.getUserID()).orElseThrow().getUsername()+" was Banned.");
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
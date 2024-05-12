package ch.jchat.chatapp.controller.api.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.jchat.chatapp.misc.RandomGenerator;
import ch.jchat.chatapp.misc.UserAuth;
import ch.jchat.chatapp.misc.Validator;
import ch.jchat.chatapp.models.Chat;
import ch.jchat.chatapp.models.Invite;
import ch.jchat.chatapp.models.Membership;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.models.dto.MembershipDto;
import ch.jchat.chatapp.repositories.ChatRepository;
import ch.jchat.chatapp.repositories.InviteRepository;
import ch.jchat.chatapp.repositories.MembershipRepository;
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
    private final UserAuth userAuth; 
    private final MembershipRepository membershipRepository;
    private final MembershipController membershipController;
    private final InviteController inviteController;
    private final InviteRepository inviteRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createChat(@RequestBody Chat chat){
        User currentUser = userAuth.getUser();
        Chat newChat = new Chat();
        newChat.setOwner(currentUser.getUserID());
        newChat.setCreationDate(new Date());
        newChat.setUserLimit((chat.getUserLimit()<255)?chat.getUserLimit():10);
        newChat.setChatName(chat.getChatName());
        Chat nc = chatRepository.save(newChat);

        String invName = RandomGenerator.generateRandomString(10);
        while (inviteRepository.existsByInviteName(invName)) {
            invName = RandomGenerator.generateRandomString(10);
        }

        Invite inv = new Invite();
        inv.setActive(true);
        inv.setChatID(nc.getChatID());
        inv.setInviteName(invName);
        inv.setInvitedByUser(currentUser.getUserID());
        inviteController.createInvite(inv);


        log.debug("Created Chat: "+newChat.toString());
        membershipController.joinChat(invName);
        return ResponseEntity.ok("Chat "+chat.getChatName()+", was created.");
    }
    @PostMapping("/limit")
    public ResponseEntity<String> changeUserLimit(@RequestBody Chat chat){
        User currentUser = userAuth.getUser();
        Chat target = chatRepository.findByChatID(chat.getChatID()).orElseThrow();
        log.debug("Setting ChannelLimit to "+chat.getUserLimit());
        if (target.getOwner()==currentUser.getUserID()) {
            target.setUserLimit(Validator.isValidLimit(chat.getUserLimit())?chat.getUserLimit():2);
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
        if (target.getOwner()==currentUser.getUserID()) {
            target.setChatName(chat.getChatName());
            chatRepository.save(target);
            return ResponseEntity.ok("Chatname set to "+chat.getChatName());
        }
        return ResponseEntity.badRequest().body("Not Authorized to change the Chatname");
    }
    @PostMapping("/delete")
    public ResponseEntity<String> deleteChat(@RequestBody Chat chat){
        User currentUser = userAuth.getUser();
        if (currentUser.getUserID()==chat.getOwner()
        && passwordEncoder.matches(userRepository.findByUserID(chat.getOwner()).get().getPassword(), 
                                    userRepository.findByUserID(currentUser.getUserID()).get().getPassword())) {
            chatRepository.delete(chat);
            return new ResponseEntity<>(chat.getChatName()+" was deleted.",HttpStatus.OK);
        }
        return new ResponseEntity<>("You are not Authorized to delete this Chat!",HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/get")
    public ResponseEntity<?> getChat() {
        User currentUser = userAuth.getUser();

        List<Membership> memberships = membershipRepository.findByUserIDAndBannedFalse(currentUser.getUserID());

        if (memberships.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No chats found yet.");
        }

        List<MembershipDto> tempList = new ArrayList<>();

        for (Membership membership : memberships) {
            chatRepository.findById(membership.getChatID()).ifPresent(chat -> {
                userRepository.findById(chat.getOwner()).ifPresent(owner -> {
                    MembershipDto newMember = new MembershipDto();
                    newMember.setChatId(chat.getChatID());
                    newMember.setChatName(chat.getChatName());
                    newMember.setMembershipId(membership.getMembershipID());
                    newMember.setOwner(owner.getUsername());
                    newMember.setInvite((inviteRepository.findByChatID(chat.getChatID()).isPresent())?inviteRepository.findByChatID(chat.getChatID()).get().getInviteName():"Test");
                    tempList.add(newMember);
                    log.debug("Added new member to list: " + newMember);
                });
            });
        }
        if (tempList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active chats available.");
        }
        return ResponseEntity.ok(tempList);
    }


}
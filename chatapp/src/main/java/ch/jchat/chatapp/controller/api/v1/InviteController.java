package ch.jchat.chatapp.controller.api.v1;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.jchat.chatapp.misc.UserAuth;
import ch.jchat.chatapp.models.Chat;
import ch.jchat.chatapp.models.Invite;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.ChatRepository;
import ch.jchat.chatapp.repositories.InviteRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/invite")
public class InviteController {
    
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private InviteRepository inviteRepository;
    @Autowired
    private UserAuth userAuth;

    @PostMapping("/create")
    public ResponseEntity<String> createInvite(@RequestBody Invite invite){
        User currentUser = userAuth.getUser();
        Chat chat = chatRepository.findByChatID(invite.getChat().getChatID()).orElseThrow();

        if (chat.getOwner().getUserID() != currentUser.getUserID()) {
            return new ResponseEntity<>("Unauthorized.",HttpStatus.UNAUTHORIZED);
        }
        if (inviteRepository.existsByChatChatID(chat.getChatID())) {
            return new ResponseEntity<>("An invite for this Chat already exists.",HttpStatus.CONFLICT);
        }
        if (inviteRepository.existsByInviteName(invite.getInviteName()) || invite.getInviteName().isBlank()) {
            return new ResponseEntity<>("Could not create a Invite with this name.",HttpStatus.CONFLICT);
        }

        Invite newInvite = new Invite();
        newInvite.setActive(true);
        newInvite.setChat(chat);
        newInvite.setExpirationDate(new Date(Long.valueOf("7956915742") ));
        newInvite.setInviteName(invite.getInviteName());
        newInvite.setInvitedByUser(chat.getOwner());
        inviteRepository.save(newInvite);
        
        return ResponseEntity.ok("Inivte: "+invite.getInviteName()+", was created.");
    }
    @PostMapping("/update")
    public ResponseEntity<String> updateInvite(@RequestBody Long oldInviteID, Invite invite){
        User currentUser = userAuth.getUser();
        Chat chat = chatRepository.findByChatID(oldInviteID).orElseThrow();
        Optional<Invite> oldInvite = inviteRepository.findByInviteID(oldInviteID);

        log.debug(chat.toString()+"\n"+currentUser.toString());
        if (chat.getOwner().getUserID() != currentUser.getUserID()) {
            return new ResponseEntity<>("Unauthorized.",HttpStatus.UNAUTHORIZED);
        }
        if (inviteRepository.existsByChatChatID(chat.getChatID())) {
            return new ResponseEntity<>("An invite for this Chat already exists.",HttpStatus.CONFLICT);
        }
        if (inviteRepository.existsByInviteName(invite.getInviteName()) || invite.getInviteName().isBlank()) {
            return new ResponseEntity<>("Could not create a Invite with this name.",HttpStatus.CONFLICT);
        }
        if (!inviteRepository.existsByInviteID(oldInviteID)) {
            return new ResponseEntity<>("Could not create a Invite with this ID.",HttpStatus.BAD_REQUEST);
        }

        try {
            oldInvite.get().setActive(invite.isActive());
            oldInvite.get().setExpirationDate(invite.getExpirationDate());
            oldInvite.get().setInviteName(invite.getInviteName());
            oldInvite.get().setInvitedByUser(chat.getOwner());
            inviteRepository.save(oldInvite.get());
            return new ResponseEntity<>("Updated the Invite",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not update the Invite.",HttpStatus.BAD_REQUEST);
        }
    }
}

    
    /*
     * (c change (multi) / s set (single) / d delete)
     * InviteID sd
     * ChatID s
     * InvitedByUserID sd
     * InviteName c
     * ExpirationDate c
     * Active c
     * 
     * create s
     * modify c
     * delete s
    */

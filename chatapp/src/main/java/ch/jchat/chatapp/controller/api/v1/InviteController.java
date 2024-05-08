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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/invite")
@AllArgsConstructor
public class InviteController {
    
    private final ChatRepository chatRepository;
    private final InviteRepository inviteRepository;
    private final UserAuth userAuth;

    @PostMapping("/create")
    public ResponseEntity<String> createInvite(@RequestBody Invite invite){
        User currentUser = userAuth.getUser();
        Chat chat = chatRepository.findByChatID(invite.getChatID()).orElseThrow();
        Optional<Invite> inv = inviteRepository.findByChatID(invite.getChatID());

        if (chat.getOwner() != currentUser.getUserID()) {
            return new ResponseEntity<>("Unauthorized.",HttpStatus.UNAUTHORIZED);
        }
        if (inv.isPresent()) {
            return new ResponseEntity<>("An Invite for this Channel already exists.", HttpStatus.CONFLICT);            
        }
        if (inviteRepository.existsByChatID(chat.getChatID())) {
            return new ResponseEntity<>("An invite for this Chat already exists.",HttpStatus.CONFLICT);
        }
        if (inviteRepository.existsByInviteName(invite.getInviteName()) || invite.getInviteName().isBlank()) {
            return new ResponseEntity<>("Could not create a Invite with this name.",HttpStatus.CONFLICT);
        }

        Invite newInvite = new Invite();
        newInvite.setActive(true);
        newInvite.setChatID(chat.getChatID());
        newInvite.setExpirationDate(new Date(Long.valueOf("7956915742") ));
        newInvite.setInviteName(invite.getInviteName());
        newInvite.setInvitedByUser(chat.getOwner());
        inviteRepository.save(newInvite);
        
        return ResponseEntity.ok("Inivte: "+invite.getInviteName()+", was created.");
    }
    @PostMapping("/update")
    public ResponseEntity<String> updateInvite(@RequestBody Invite invite){
        User currentUser = userAuth.getUser();
        Optional<Invite> oi = inviteRepository.findByChatID(invite.getChatID());
        Invite oldInvite;
        if(oi.isPresent()){oldInvite = oi.get();}else{return new ResponseEntity<>("Invite does not exist.",HttpStatus.BAD_REQUEST);}

        Chat chat = chatRepository.findByChatID(oldInvite.getChatID()).orElseThrow();

        log.debug(chat.toString()+"\n"+currentUser.toString());
        if (chat.getOwner() != currentUser.getUserID()) {
            return new ResponseEntity<>("Unauthorized.",HttpStatus.UNAUTHORIZED);
        }
        if (inviteRepository.existsByInviteName(invite.getInviteName()) || invite.getInviteName().isBlank()) {
            return new ResponseEntity<>("Could not create a Invite with this name.",HttpStatus.CONFLICT);
        }

        try {
            oldInvite.setActive((invite.isActive()));
            oldInvite.setExpirationDate(invite.getExpirationDate());
            oldInvite.setInviteName(invite.getInviteName());
            oldInvite.setInvitedByUser(chat.getOwner());
            inviteRepository.save(oldInvite);
            return new ResponseEntity<>("Updated the Invite",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not update the Invite.",HttpStatus.BAD_REQUEST);
        }
    }
}

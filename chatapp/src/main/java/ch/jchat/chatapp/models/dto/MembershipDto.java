package ch.jchat.chatapp.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipDto {

    private Long chatId;
    private Long membershipId;
    private String owner;
    private String chatName;
    private String invite;
}

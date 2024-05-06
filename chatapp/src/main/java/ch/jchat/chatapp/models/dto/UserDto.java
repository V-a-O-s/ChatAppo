package ch.jchat.chatapp.models.dto;

import java.util.Date;

import ch.jchat.chatapp.enums.EAvatar;
import ch.jchat.chatapp.enums.EPlatformRoles;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String Username;
    private Date creationDate;
    private EPlatformRoles platformRole;
    private EAvatar avatar;
}

package ch.jchat.chatapp.models.dto;

import java.util.Date;

import ch.jchat.chatapp.enums.EAvatar;
import ch.jchat.chatapp.enums.EPlatformRoles;
import ch.jchat.chatapp.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long userID;
    private String Username;
    private Date creationDate;
    private EPlatformRoles platformRole;
    private EAvatar avatar;

    public UserDto toDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUserID(user.getUserID());
        userDto.setUsername(user.getUsername());
        userDto.setCreationDate(user.getCreationDate());
        userDto.setPlatformRole(user.getRole());
        userDto.setAvatar(user.getAvatar());
        return userDto;
    }
}

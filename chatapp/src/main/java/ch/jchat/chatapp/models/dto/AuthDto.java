package ch.jchat.chatapp.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
    private Long userID;
    private String username;
    private String password;
    private String email;
    private String backUpEmail;
    private String phone;
}

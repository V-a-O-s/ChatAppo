package ch.jchat.chatapp.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
    private String password;
    private String newPassword;
}

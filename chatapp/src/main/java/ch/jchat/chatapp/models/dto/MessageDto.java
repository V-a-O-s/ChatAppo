package ch.jchat.chatapp.models.dto;

import java.time.LocalDateTime;
import java.util.Date;

import ch.jchat.chatapp.enums.EAvatar;
import lombok.Data;

@Data
public class MessageDto {
    private Long messageid;
    private Long chatId;
    private Long userId;
    private EAvatar avatar;
    private String username;
    private String messageText;
    private Date sendingTime;
}

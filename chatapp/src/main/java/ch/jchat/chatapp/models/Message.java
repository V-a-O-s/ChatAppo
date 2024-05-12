package ch.jchat.chatapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageid;

    //@ManyToOne
    //@JoinColumn(name = "chatID", nullable = false)
    @NotNull
    @Column(name = "chatID")
    private Long chatid;

    //@ManyToOne
    //@NotNull
    //@JoinColumn(name = "userID", nullable = false)
    @NotNull
    @Column(name = "userID")
    private Long userid;

    @NotBlank()
    private String messageText;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendingTime;
}
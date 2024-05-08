package ch.jchat.chatapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "Invites")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inviteID;

    //@NotNull
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "chatID", nullable = false)
    @Column(name = "chatID")
    private Long chatID;

//    @NotNull
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "invitedByUserID", nullable = false)
    @Column(name = "invitedByUserID")
    private Long invitedByUser;

    @NotNull
    @Column(name = "inviteName", length = 255, nullable = false)
    private String inviteName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;
}

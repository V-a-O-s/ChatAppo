package ch.jchat.chatapp.models;

import java.util.Date;

import ch.jchat.chatapp.enums.EChatRoles;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Memberships")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipID;

    @ManyToOne
    @JoinColumn(name = "chatID", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private EChatRoles userRole;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joinDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date chatTimeout;

    private boolean banned;

    @Temporal(TemporalType.TIMESTAMP)
    private Date banDate;

    private String banReason;
}

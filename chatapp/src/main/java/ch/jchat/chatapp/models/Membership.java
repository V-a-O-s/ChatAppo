package ch.jchat.chatapp.models;

import java.time.LocalDateTime;
import java.util.Date;

import ch.jchat.chatapp.enums.EChatRoles;
import jakarta.persistence.Column;
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
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ToString
@Table(name = "Memberships")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chatID", nullable = false)
    private Chat chat;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private EChatRoles userRole;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime joinDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime chatTimeout;

    private boolean banned;

    @Temporal(TemporalType.TIMESTAMP)
    private Date banDate;

    private String banReason;
}

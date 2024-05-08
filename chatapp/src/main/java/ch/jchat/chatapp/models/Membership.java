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
@ToString
@Entity
@Table(name = "Memberships")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipID;

    @Column(name = "userID", nullable = false)
    private Long userID;  // Treat as foreign key ID to User entity

    @Column(name = "chatID", nullable = false)
    private Long chatID;  // Treat as foreign key ID to Chat entity

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private EChatRoles userRole;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime joinDate;

    private boolean banned;

    @Column(columnDefinition = "TIMESTAMP")
    private Date banDate;

    private String banReason;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime chatTimeout;
}

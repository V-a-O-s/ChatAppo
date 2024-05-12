package ch.jchat.chatapp.models.auth;

import ch.jchat.chatapp.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="tokens")
public class Token {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tokenID", nullable = false)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "logged_out", nullable = false)
    private boolean loggedOut;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;
}

package ch.jchat.chatapp.models;

import java.util.Date;

import ch.jchat.chatapp.enums.EAvatar;
import ch.jchat.chatapp.enums.EPlatformRoles;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @NotBlank
    @Column(length = 255)
    private String username;

    @NotBlank
    @Column(length = 255)
    private String passwordHash;

    @NotBlank
    @Column(length = 255)
    private String salt;

    @NotBlank
    @Email
    @Column(length = 255)
    private String email;

    @Email
    @Column(length = 255)
    private String backUpEmail;

    @Column(length = 20)
    private String phone;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @NotNull
    private boolean banned;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EPlatformRoles role;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EAvatar avatar;
}
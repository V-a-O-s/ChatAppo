package ch.jchat.chatapp.misc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class AppConfig {

    @Value("${myapp.jwtSecret:N/A}")
    private String jwtSecret;

    @Value("${myapp.jwtExpirationMs}")
    private Long jwtExpiration;

    @Value("${myapp.appName}")
    private String appName;

}

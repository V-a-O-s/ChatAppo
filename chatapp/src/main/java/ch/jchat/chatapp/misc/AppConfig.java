package ch.jchat.chatapp.misc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${myapp.jwtSecret:N/A}")
    private String jwtSecret;

    public String getJwtSecret() {
        return jwtSecret;
    }
}

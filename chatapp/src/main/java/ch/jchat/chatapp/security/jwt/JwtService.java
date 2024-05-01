package ch.jchat.chatapp.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ch.jchat.chatapp.misc.AppConfig;
import ch.jchat.chatapp.models.User;
import ch.jchat.chatapp.repositories.TokenRepository;

@Service
@AllArgsConstructor
public class JwtService {

    private final TokenRepository tokenRepository;

    @Autowired
    private AppConfig appConfig;
    //private final String SECRET_KEY = appConfig.getJwtSecret();

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user){
        String username = extractUsername(token);
        boolean isValidToken = tokenRepository.findByToken(token).map(t->!t.isLoggedOut()).orElse(false);
        return (username.equals(user.getUsername()) && !isTokenExpired(token) && isValidToken);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
        .parser()
        .verifyWith(getSigninKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }

    public String generateToken(User user){
        String token = Jwts
        .builder()
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
        .signWith(getSigninKey())
        .compact();
        return token;
    }
    
    private SecretKey getSigninKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(appConfig.getJwtSecret());
        return Keys.hmacShaKeyFor(keyBytes);

    }
}

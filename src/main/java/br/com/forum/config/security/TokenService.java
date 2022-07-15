package br.com.forum.config.security;

import br.com.forum.model.UserForum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String generateToken(Authentication authentication) {
        UserForum userForum = (UserForum) authentication.getPrincipal();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationDate = now.plusSeconds(Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("Fórum")
                .setSubject(userForum.getId().toString())
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private Jws<Claims> parseClaimsJws(String token) throws Exception {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);
    }

    public boolean isValidToken(String token) {
        try {
            parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getIdUserForum(String token) {
        try {
            Claims claims = parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}

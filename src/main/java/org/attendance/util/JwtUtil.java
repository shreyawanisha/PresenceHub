package org.attendance.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String subject){
        long expirationTime = 1000 * 60 * 60; // 1 hour

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("⚠️ Token expired");
        } catch (MalformedJwtException e) {
            System.out.println("⚠️ Malformed token");
        } catch (UnsupportedJwtException e) {
            System.out.println("⚠️ Unsupported token");
        } catch (Exception e) {
            System.out.println("⚠️ Invalid token: " + e.getMessage());
        }
        return false;
    }

    public String extractUsernameFromToken(String token) {
       return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody().getSubject();
    }
}

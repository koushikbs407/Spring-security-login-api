package com.SpringSecurity.SpringSecurity.Config;

import com.SpringSecurity.SpringSecurity.User.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    final static String SECRET_Key = "vR8uP3NcA7t+Q3K4wL5m2F0oXsVrHbYiQlJ4CpUk5T8=";



    public String extractName(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, user);
    }


    public String generateToken(Map<String, Object> claims, User user) {

        return Jwts.builder().setClaims(claims)
                .setSubject(user.getUsername()).
                setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *60*24))
                .signWith(SignatureAlgorithm.HS256,getSignatureKey()).compact();
    }

    public Boolean validateToken(String token, UserDetails user) {
        final String username = extractName(token);

        return(username.equals(user.getUsername())  && istokenexpired(token));
    }

    private boolean istokenexpired(String token) {
        Claims claims1 = extractClaims(token);
        Date expiration = claims1.getExpiration();
        Date now = new Date();
        return expiration.before(now);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final  Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractClaims(String token) {

        return Jwts.parser().setSigningKey(getSignatureKey())
                .parseClaimsJws(token).
                getBody();
    }

    private Key getSignatureKey() {
       byte[] keyBytes = Base64.getDecoder().decode(SECRET_Key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

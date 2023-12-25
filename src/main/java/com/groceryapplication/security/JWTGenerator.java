package com.groceryapplication.security;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;


import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JWTGenerator {

    private static final String AUTHORITIES_KEY = "ROLE";

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact();
    }
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch ( Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }

    public String getUsernameFromToken (String bearerToken) {

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
           return getUsernameFromJWT(bearerToken.substring(7));
        }
        return  null;
    }
}
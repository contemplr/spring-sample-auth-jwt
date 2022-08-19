package com.auth.example.securities;

import com.auth.example.entites.TokenStore;
import com.auth.example.services.repositories.TokenStoreRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultHeader;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${service.jwt.secret:sample}")
    private String jwtSecret;

    @Value("${service.jwt.exp:7200}")
    private int jwtExpiration;

    @Value("${service.jwt.issuer:contvarAppName}")
    private String jwtIssuer;

    private final TokenStoreRepository tokenStoreRepository;


    public Tokens generateJwtToken(long userId, String username) {
        long expirationTime = jwtExpiration * 1000L;
        String id = UUID.randomUUID().toString();

        Date issuedDate = new Date();
        Date expirationDate = new Date((issuedDate.getTime() + expirationTime));

        JwtBuilder jwtBuilder = Jwts.builder().setIssuer(jwtIssuer).setId(id)
                .setSubject(username).setIssuedAt(issuedDate)
                .claim("uid", userId);

        DefaultHeader<?> header = new DefaultHeader<>();
        header.setType("access");
        String accessToken = jwtBuilder.setHeader((Map<String, Object>) header)
                .setExpiration(new Date((issuedDate.getTime() + expirationTime)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        header.setType("refresh");
        String refreshToken = jwtBuilder.setHeader((Map<String, Object>) header)
                .setNotBefore(new Date((expirationDate.getTime() - expirationTime/2)))
                .setExpiration(new Date((issuedDate.getTime() + 1209600000L))) // expire in 14 days
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // keep track of generated token
        CompletableFuture.runAsync(() -> tokenStoreRepository.save(TokenStore.newInstance(id, userId)));
        return Tokens.builder().access(accessToken).refresh(refreshToken).build();
    }

    private Jws<Claims> parseToken(String token){
        if(!StringUtils.hasText(token))
            throw new JwtException("Invalid token provided");
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
    }
    public boolean validateAccessJwtToken(String authToken) {
        return Objects.equals(parseToken(authToken).getHeader().getType(), "access");
    }

    public boolean validateRefreshJwtToken(String authToken) {
        return Objects.equals(parseToken(authToken).getHeader().getType(), "refresh");
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public Long getUserIdFromJwtToken(String token) {
        return parseToken(token).getBody().get("uid", Long.class);
    }

    @Getter
    @Setter
    @Builder
    public static class Tokens {
        private String access;
        private String refresh;
    }
}


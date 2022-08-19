package com.auth.example.entites;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "token_store")
public class TokenStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String tokenId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private boolean enabled = true;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    public static TokenStore newInstance(String id, long userId) {
        TokenStore tokenStore = new TokenStore();
        tokenStore.setTokenId(id);
        tokenStore.setUserId(userId);
        return tokenStore;
    }
}

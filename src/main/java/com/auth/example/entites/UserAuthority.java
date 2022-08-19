package com.auth.example.entites;

import com.auth.example.enums.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "authorities")
public class UserAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(nullable = false)
    private long userId;

    public UserAuthority(Authority authority, long userId){
        this.authority = authority;
        this.userId = userId;
    }
}

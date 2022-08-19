package com.auth.example.controllers.dtos;

import com.auth.example.enums.Authority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@RequiredArgsConstructor
public class GrantedAuthorityDto implements GrantedAuthority {

    private final Authority authority;

    @Override
    public String getAuthority() {
        return authority.name();
    }

    public static GrantedAuthority toGrantedAuthority(Authority authority){
        return new GrantedAuthorityDto(authority);
    }
}

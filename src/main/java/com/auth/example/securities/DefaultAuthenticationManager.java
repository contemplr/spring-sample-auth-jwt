package com.auth.example.securities;

import com.auth.example.controllers.dtos.GrantedAuthorityDto;
import com.auth.example.entites.User;
import com.auth.example.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DefaultAuthenticationManager implements AuthenticationManager {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userService.findUserByUsername(authentication.getName());
        Set<GrantedAuthority> authorities = userService.findUserAuthorityByUserId(user.getId())
                .stream().map(GrantedAuthorityDto::toGrantedAuthority).collect(Collectors.toSet());
        if(!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())){
            throw new Exception("invalid.login.credentials");
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),
                authorities);
    }
}

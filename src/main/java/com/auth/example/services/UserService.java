package com.auth.example.services;

import com.auth.example.entites.User;
import com.auth.example.entites.UserAuthority;
import com.auth.example.enums.Authority;
import com.auth.example.services.repositories.UserAuthorityRepository;
import com.auth.example.services.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username);
    }

    @SneakyThrows
    public User findUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("user.not.found"));
    }

    public Set<Authority> findUserAuthorityByUserId(long id) {
        return userAuthorityRepository.findAllByUserId(id)
                .stream().map(UserAuthority::getAuthority).collect(Collectors.toSet());
    }
}

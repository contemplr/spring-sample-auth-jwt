package com.auth.example.services;

import com.auth.example.controllers.dtos.RegisterDto;
import com.auth.example.controllers.dtos.UserDto;
import com.auth.example.entites.User;
import com.auth.example.entites.UserAuthority;
import com.auth.example.enums.Authority;
import com.auth.example.services.repositories.UserAuthorityRepository;
import com.auth.example.services.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthorityRepository userAuthorityRepository;

    @SneakyThrows
    public void signup(RegisterDto registerDto) {
        if(userRepository.existsByEmailAddressOrPhoneNum(registerDto.getEmail()))
            throw new Exception("username.already.exists");

        String username = RandomStringUtils.randomAlphanumeric(8, 12);
        String password = passwordEncoder.encode(registerDto.getPassword());

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user = userRepository.save(user);

        Set<UserAuthority> userAuthorities = new HashSet<>();
        userAuthorities.add(new UserAuthority(Authority.AUTHED, user.getId()));
        userAuthorityRepository.saveAll(userAuthorities);
    }

    @SneakyThrows
    public UserDto findUsernameByEmailOrPhone(String username) {
        UserDto userDto = new UserDto();
        User user = userRepository.findUsernameByEmailAddressOrPhoneNum(username)
                .orElseThrow(() -> new Exception("user.not.found"));
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}

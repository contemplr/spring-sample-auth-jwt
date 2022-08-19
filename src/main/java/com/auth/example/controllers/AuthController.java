package com.auth.example.controllers;

import com.auth.example.controllers.dtos.LoginDto;
import com.auth.example.controllers.dtos.RegisterDto;
import com.auth.example.controllers.dtos.UserDto;
import com.auth.example.securities.JwtProvider;
import com.auth.example.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    private void appendJwtTokens(long userId, String username, HttpServletResponse httpServletResponse){
        JwtProvider.Tokens tokens = jwtProvider.generateJwtToken(userId, username);
        httpServletResponse.setHeader("contvarX-Access-Token", tokens.getAccess());
        httpServletResponse.setHeader("contvarX-Refresh-Token", tokens.getRefresh());
    }

    @PostMapping("register")
    public void register(@RequestBody @Valid RegisterDto registerDto, HttpServletResponse httpServletResponse) {
        authService.signup(registerDto);
        appendJwtTokens(registerDto.getUserId(), registerDto.getUsername(), httpServletResponse);
    }

    @PostMapping("authorize")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authorize(@RequestBody @Valid LoginDto loginDto, HttpServletResponse httpServletResponse) {
        UserDto userDto = authService.findUsernameByEmailOrPhone(loginDto.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), loginDto.getPassword())
        );
        appendJwtTokens(userDto.getId(), authentication.getName(), httpServletResponse);
    }

    @SneakyThrows
    @PostMapping("reauthorize")
    public void reauthorize(@RequestHeader("contvarX-Refresh-Token") String refreshToken,
                            HttpServletResponse httpServletResponse) {
        if(jwtProvider.validateRefreshJwtToken(refreshToken)) {
            String username = jwtProvider.getUserNameFromJwtToken(refreshToken);
            long uid = jwtProvider.getUserIdFromJwtToken(refreshToken);
            appendJwtTokens(uid, username, httpServletResponse);
        }
        throw new Exception("invalid.refresh.token");
    }
}

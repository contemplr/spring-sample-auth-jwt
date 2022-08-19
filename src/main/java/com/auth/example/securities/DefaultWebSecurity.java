package com.auth.example.securities;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class DefaultWebSecurity {

    private final JwtAuthTokenFilter jwtAuthTokenFilter;
    private final AuthenticationManager authenticationManager;

    private final String[] allowed_unauthorized_routes = {
            "/register", "/authorize", "/reauthorize"
    };

    private final String[] allowed_unauthorized_routes_get = {

    };

    private final String[] allowed_unauthorized_routes_post = {

    };

    private final String[] allowed_unauthorized_routes_put = {

    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(allowed_unauthorized_routes)
                .permitAll()
                .antMatchers(HttpMethod.POST, allowed_unauthorized_routes_post)
                .permitAll()
                .antMatchers(HttpMethod.GET, allowed_unauthorized_routes_get)
                .permitAll()
                .antMatchers(HttpMethod.PUT, allowed_unauthorized_routes_put)
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationManager(authenticationManager)
                .exceptionHandling()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

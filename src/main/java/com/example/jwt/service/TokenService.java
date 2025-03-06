package com.example.jwt.service;

import com.example.jwt.auth.util.AuthenticationScheme;
import com.example.jwt.auth.util.JwtProvider;
import com.example.jwt.dto.AccessTokenResDto;
import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public AccessTokenResDto getNewAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh 토큰이 존재하지 않습니다.");
        }

        String email = jwtProvider.getUsername(refreshToken);
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 이메일이 존재하지 않습니다."));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(user.getRole().name())));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String accessToken = jwtProvider.generateAccessToken(authenticationToken);

        return new AccessTokenResDto(AuthenticationScheme.BEARER.getName(), accessToken);
    }
}
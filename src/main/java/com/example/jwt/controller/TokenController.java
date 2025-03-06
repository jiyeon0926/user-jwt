package com.example.jwt.controller;

import com.example.jwt.dto.AccessTokenResDto;
import com.example.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    // AccessToken 재발급
    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResDto> getNewAccessToken(@CookieValue(required = false) String refreshToken) {
        AccessTokenResDto getNewAccessToken = tokenService.getNewAccessToken(refreshToken);

        return new ResponseEntity<>(getNewAccessToken, HttpStatus.OK);
    }
}
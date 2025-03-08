package com.example.jwt.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccessTokenResDto {

    private final String tokenAuthScheme;
    private final String accessToken;
}

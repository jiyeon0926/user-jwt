package com.example.jwt.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDetailResDto {

    private final Long id;
    private final String email;
    private final String nickname;
}
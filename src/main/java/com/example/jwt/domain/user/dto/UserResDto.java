package com.example.jwt.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserResDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String role;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}

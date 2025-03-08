package com.example.jwt.global.auth.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthenticationScheme {
    BEARER("Bearer");

    private final String name;

    /**
     * Authorization 헤더 값으로 사용될 prefix 생성
     * @param authenticationScheme
     * @return 생성된 prefix
     */
    public static String generateType(AuthenticationScheme authenticationScheme) {
        return authenticationScheme.getName() + " ";
    }
}
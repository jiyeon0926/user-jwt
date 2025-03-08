package com.example.jwt.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 BAD_REQUEST
    PASSWORD_BAD_REQUEST(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),

    // 401 UNAUTHORIZED
    REFRESH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Refresh 토큰이 존재하지 않거나, 유효하지 않습니다."),

    // 403 FORBIDDEN
    USER_NOT_MATCH(HttpStatus.FORBIDDEN, "사용자가 일치하지 않습니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일이 존재하지 않습니다."),

    // 409 CONFLICT
    EMAIL_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
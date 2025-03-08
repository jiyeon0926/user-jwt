package com.example.jwt.global.auth.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieUtil {

    public void addHeaderCookie(HttpServletResponse response,
                                String name,
                                String value,
                                String path,
                                int maxAge) {
        ResponseCookie responseCookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path(path)
                .maxAge(Duration.ofSeconds(maxAge))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public void deleteCookie(HttpServletResponse response, String name, String path) {
        ResponseCookie responseCookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .path(path)
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}
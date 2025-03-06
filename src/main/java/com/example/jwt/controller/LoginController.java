package com.example.jwt.controller;

import com.example.jwt.dto.LoginReqDto;
import com.example.jwt.dto.LoginResDto;
import com.example.jwt.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto loginReqDto,
                                             HttpServletResponse response) {
        LoginResDto login = userService.login(loginReqDto.getEmail(), loginReqDto.getPassword());

        ResponseCookie newRefreshTokenCookie = ResponseCookie.from("refreshToken", login.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(5))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());

        return new ResponseEntity<>(login, HttpStatus.OK);
    }
}
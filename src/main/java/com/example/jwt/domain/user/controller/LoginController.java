package com.example.jwt.domain.user.controller;

import com.example.jwt.global.auth.util.CookieUtil;
import com.example.jwt.domain.user.dto.LoginReqDto;
import com.example.jwt.domain.user.dto.LoginResDto;
import com.example.jwt.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto loginReqDto,
                                             HttpServletResponse response) {
        LoginResDto login = userService.login(loginReqDto.getEmail(), loginReqDto.getPassword());

        String name = "refreshToken";
        String path = "/";
        int maxAge = 300;

        cookieUtil.addHeaderCookie(response, name, login.getRefreshToken(), path, maxAge);

        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        String name = "refreshToken";
        String path = "/";

        cookieUtil.deleteCookie(response, name, path);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
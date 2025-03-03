package com.example.jwt.controller;

import com.example.jwt.dto.UserResDto;
import com.example.jwt.dto.UserSignupReqDto;
import com.example.jwt.service.UserService;
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
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResDto> signup(@Valid @RequestBody UserSignupReqDto userSignupReqDto) {
        UserResDto signup = userService.signup(
                userSignupReqDto.getEmail(),
                userSignupReqDto.getPassword(),
                userSignupReqDto.getNickname(),
                userSignupReqDto.getRole());

        return new ResponseEntity<>(signup, HttpStatus.CREATED);
    }
}
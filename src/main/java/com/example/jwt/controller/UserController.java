package com.example.jwt.controller;

import com.example.jwt.dto.UserDetailResDto;
import com.example.jwt.dto.UserResDto;
import com.example.jwt.dto.UserSignupReqDto;
import com.example.jwt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 사용자 단 건 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailResDto> findUserById(@PathVariable Long userId) {
        UserDetailResDto findUserById = userService.findUserById(userId);

        return new ResponseEntity<>(findUserById, HttpStatus.OK);
    }

    // 사용자 전체 조회
    @GetMapping
    public ResponseEntity<List<UserDetailResDto>> findAll() {
        List<UserDetailResDto> findAll = userService.findAll();

        return new ResponseEntity<>(findAll, HttpStatus.OK);
    }
}
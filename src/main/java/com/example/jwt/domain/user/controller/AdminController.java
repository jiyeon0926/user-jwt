package com.example.jwt.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @GetMapping("/access")
    public ResponseEntity<String> access() {
        return new ResponseEntity<>("관리자만 접근할 수 있습니다.", HttpStatus.OK);
    }
}
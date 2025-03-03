package com.example.jwt.service;

import com.example.jwt.dto.UserResDto;
import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserResDto signup(String email, String password, String nickname, String role) {
        userRepository.findUserByEmail(email)
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
                });

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, nickname, role.toUpperCase());
        User savedUser = userRepository.save(user);

        return new UserResDto(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getNickname(),
                savedUser.getRole().name(),
                savedUser.getCreatedAt(),
                savedUser.getModifiedAt()
        );
    }
}
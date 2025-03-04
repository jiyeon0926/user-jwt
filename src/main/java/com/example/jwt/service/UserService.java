package com.example.jwt.service;

import com.example.jwt.auth.util.AuthenticationScheme;
import com.example.jwt.auth.util.JwtProvider;
import com.example.jwt.dto.LoginResDto;
import com.example.jwt.dto.UserDetailResDto;
import com.example.jwt.dto.UserResDto;
import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

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

    @Transactional(readOnly = true)
    public UserDetailResDto findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        return new UserDetailResDto(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }

    @Transactional(readOnly = true)
    public List<UserDetailResDto> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserDetailResDto(
                        user.getId(),
                        user.getEmail(),
                        user.getNickname()))
                .collect(Collectors.toList());
    }

    public LoginResDto login(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 이메일이 존재하지 않습니다."));

        validatePassword(password, user.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateToken(authentication);

        return new LoginResDto(AuthenticationScheme.BEARER.getName(), accessToken);
    }

    private void validatePassword(String rawPassword, String encodedPassword) throws IllegalArgumentException {
        boolean notValid = !passwordEncoder.matches(rawPassword, encodedPassword);

        if (notValid) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }
    }
}
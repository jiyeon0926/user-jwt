package com.example.jwt.service;

import com.example.jwt.auth.util.AuthenticationScheme;
import com.example.jwt.auth.util.JwtProvider;
import com.example.jwt.common.exception.CustomException;
import com.example.jwt.common.exception.ErrorCode;
import com.example.jwt.dto.LoginResDto;
import com.example.jwt.dto.UserDetailResDto;
import com.example.jwt.dto.UserResDto;
import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    throw new CustomException(ErrorCode.EMAIL_EXISTS);
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
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return new UserDetailResDto(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }

    @Transactional(readOnly = true)
    public List<UserDetailResDto> findAll() {
        List<User> users = userRepository.findUsersByIsDeleted(false);

        return users.stream()
                .map(user -> new UserDetailResDto(
                        user.getId(),
                        user.getEmail(),
                        user.getNickname()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long userId, String bearerToken) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String headerPrefix = AuthenticationScheme.generateType(AuthenticationScheme.BEARER);
        String accessToken = bearerToken.substring(headerPrefix.length());

        String email = jwtProvider.getUsername(accessToken);
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getId().equals(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_MATCH);
        }

        userRepository.delete(user);
    }

    public LoginResDto login(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));

        if (user.isDeleted()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        validatePassword(password, user.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        return new LoginResDto(AuthenticationScheme.BEARER.getName(), accessToken, refreshToken);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        boolean notValid = !passwordEncoder.matches(rawPassword, encodedPassword);

        if (notValid) {
            throw new CustomException(ErrorCode.PASSWORD_BAD_REQUEST);
        }
    }
}
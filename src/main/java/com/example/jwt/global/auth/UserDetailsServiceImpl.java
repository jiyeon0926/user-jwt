package com.example.jwt.global.auth;

import com.example.jwt.global.error.CustomException;
import com.example.jwt.global.error.ErrorCode;
import com.example.jwt.domain.user.entity.User;
import com.example.jwt.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 입력받은 이메일에 해당되는 사용자 정보를 찾아 반환
     * @param username - 사용자의 이메일을 username으로 사용
     * @return 해당되는 사용자의 UserDetailsImpl 객체
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user);
    }
}
package com.example.jwt.global.auth;

import com.example.jwt.global.common.constant.UserRole;
import com.example.jwt.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    /**
     * 사용자 권한 반환
     * @return 사용자 권한 리스트
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = user.getRole();

        return new ArrayList<>(role.getAuthorities());
    }

    /**
     * 사용자의 자격 증명 반환
     * @return 사용자 비밀번호
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 사용자의 자격 증명 반환
     * @return 사용자 이메일
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * 계정 만료 여부
     * @return 만료되지 않았으면 true, 만료되었으면 false
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금 여부
     * @return 잠겨있지 않으면 true, 잠겨있으면 false
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명 만료 여부
     * @return 만료되지 않았으면 true, 만료되었으면 false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     * @return 활성화되어 있으면 true, 비활성화 상태이면 false
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
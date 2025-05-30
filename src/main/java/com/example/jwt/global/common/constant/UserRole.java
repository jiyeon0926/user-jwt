package com.example.jwt.global.common.constant;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum UserRole {
    USER,
    ADMIN;

    public static UserRole of(String roleName) {
        for (UserRole role : values()) {
            if (role.name().equals(roleName.toUpperCase())) {
                return role;
            }
        }

        throw new IllegalArgumentException("해당하는 이름의 권한을 찾을 수 없습니다: " + roleName);
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + name()));
    }
}
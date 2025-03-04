package com.example.jwt.auth.filter;

import com.example.jwt.auth.util.AuthenticationScheme;
import com.example.jwt.auth.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    private static final List<String> WHITE_LIST = List.of("/users/signup", "/users/login");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isWhitelisted(request)) {
            filterChain.doFilter(request, response);

            return;
        }

        authenticate(request);
        filterChain.doFilter(request, response);
    }

    /**
     * request를 이용해 인증을 처리
     * @param request
     */
    private void authenticate(HttpServletRequest request) {
        String token = getTokenFromRequest(request);

        if (!jwtProvider.validToken(token)) {
            return;
        }

        String username = jwtProvider.getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        setAuthentication(request, userDetails);
    }

    /**
     * request의 Authorization 헤더에서 Token 값을 추출
     * @param request
     * @return Token 값 (찾지 못하면 null)
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String headerPrefix = AuthenticationScheme.generateType(AuthenticationScheme.BEARER);

        boolean tokenFound = StringUtils.hasText(bearerToken) && bearerToken.startsWith(headerPrefix);

        if (tokenFound) {
            return bearerToken.substring(headerPrefix.length());
        }

        return null;
    }

    /**
     * SecurityContext에 인증 객체를 저장
     * @param request
     * @param userDetails
     */
    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 화이트리스트 경로인지 확인
     * @param request
     * @return 화이트리스트에 포함되면 true, 포함되지 않으면 false
     */
    private boolean isWhitelisted(HttpServletRequest request) {
        String path = request.getRequestURI();

        return WHITE_LIST.contains(path);
    }
}
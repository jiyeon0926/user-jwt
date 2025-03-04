package com.example.jwt.auth.util;

import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.expiry-millis}")
    private long expiryMillis;

    private final UserRepository userRepository;

    /**
     * Token 생성 후 반환
     *
     * @param authentication
     * @return 생성된 Token
     * @throws EntityNotFoundException
     */
    public String generateToken(Authentication authentication) throws EntityNotFoundException {
        String username = authentication.getName();

        return generateTokenBy(username);
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);

        return claims.getSubject();
    }

    public boolean validToken(String token) throws JwtException {
        try {
            return !tokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 토큰입니다.: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰이 만료되었습니다.: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.: {}", e.getMessage());
        }

        return false;
    }

    /**
     * 이메일을 이용해 Token 생성 후 반환
     * - 토큰 생성에는 HS256 알고리즘을 이용
     *
     * @param email
     * @return 생성된 Token
     * @throws EntityNotFoundException
     */
    private String generateTokenBy(String email) throws EntityNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일이 존재하지 않습니다."));

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + expiryMillis);

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("role", user.getRole())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new MalformedJwtException("토큰이 비어 있습니다.");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean tokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return resolveClaims(token, Claims::getExpiration);
    }

    private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);

        return claimsResolver.apply(claims);
    }
}
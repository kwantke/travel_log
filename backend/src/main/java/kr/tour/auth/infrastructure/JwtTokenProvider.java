package kr.tour.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.tour.auth.dto.response.TokenResponse;
import kr.tour.global.exception.UnauthorizedException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String MEMBER_ID_KEY = "id";

    private final String accessSecretKey;
    private final String refreshSecretKey;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtTokenProvider(
            @Value("${jwt.token.secret-key}") String accessSecretKey,
            @Value("${jwt.refresh.secret-key}") String refreshSecretKey,
            @Value("${jwt.token.expire-length}") long accessExpiration,
            @Value("${jwt.refresh.expire-length}") long refreshExpiration
    ) {
        this.accessSecretKey = accessSecretKey;
        this.accessExpiration = accessExpiration;
        this.refreshSecretKey = refreshSecretKey;
        this.refreshExpiration = refreshExpiration;
    }

    public TokenResponse createToken(Long memberId) {
        String accessToken = createToken(memberId, accessSecretKey, accessExpiration);
        String refreshToken = createToken(memberId, refreshSecretKey, refreshExpiration);
        return new TokenResponse(accessToken, refreshToken);
    }

    private String createToken(Long memberId, String secretKey, long expiration) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(memberId.toString())
                .claim(MEMBER_ID_KEY, memberId)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String decodeAccessToken(String token) {
        return decode(token, accessSecretKey);
    }

    public String decodeRefreshToken(String token) {
        return decode(token, refreshSecretKey);
    }

    private String decode(String token, String secretKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get(MEMBER_ID_KEY)
                    .toString();
        } catch (ExpiredJwtException exception) {
            throw new UnauthorizedException("이미 만료된 토큰입니다.", exception.getCause());
        } catch (Exception exception) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.", exception.getCause());
        }
    }


}

package kr.tour.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.tour.global.config.JwtTokenProvider;
import kr.tour.global.dto.HttpRequestInfo;
import kr.tour.global.dto.MemberAuth;
import kr.tour.global.exception.ExceptionResponse;
import kr.tour.global.log.logger.ConsoleLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ConsoleLogger consoleLogger;


    private final List<HttpRequestInfo> whiteList;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

        if ((token == null || token.isBlank()) && isWhitelisted(request)) {

            filterChain.doFilter(request, response);
            return;
        }

        if (isTokenBlank(token)) {
            sendUnauthorizedResponse(response, "로그인을 해주세요.");
            return;
        }

        try {
            String memberId = jwtTokenProvider.decodeAccessToken(token);
            authenticateAsMember(memberId);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendUnauthorizedResponse(response, e.getMessage());
        }
    }

    private String extractTokenFromHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring("Bearer ".length());
    }

    private boolean isWhitelisted(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        return whiteList.stream()
                .anyMatch(white -> white.method().matches(method) && antPathMatcher.match(white.urlPattern(), url));
    }

    private void authenticateAsMember(String memberId) {
        MemberAuth memberAuth = MemberAuth.from(Long.parseLong(memberId));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(memberAuth, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        log.warn("UNAUTHORIZED_EXCEPTION :: message = {}", message);

        ExceptionResponse errorResponse = new ExceptionResponse(message);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.getWriter()
                .write(objectMapper.writeValueAsString(errorResponse));
    }

    private boolean isTokenBlank(String token) {
        return token == null || token.isBlank();
    }
}

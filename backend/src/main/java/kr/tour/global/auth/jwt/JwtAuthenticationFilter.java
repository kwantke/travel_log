package kr.tour.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.tour.auth.dto.request.RequestUser;
import kr.tour.auth.infrastructure.JwtTokenProvider;
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
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String MEMBER_ID_ATTRIBUTE = "memberId";
    private final ConsoleLogger consoleLogger;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> whitelist;
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

        try {
            String memberId = jwtTokenProvider.decodeAccessToken(token);
            authenticateAsMember(memberId);
            request.setAttribute(MEMBER_ID_ATTRIBUTE, memberId);
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
        String path = request.getRequestURI();
        return whitelist.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private void authenticateAsMember(String memberId) {
        RequestUser requestUser = new RequestUser(memberId);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(requestUser, null, Collections.emptyList());
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
}

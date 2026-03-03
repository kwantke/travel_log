package kr.tour.global.log;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.tour.global.exception.AutoResolvedErrorCode;
import kr.tour.global.log.logger.ConsoleLogger;
import kr.tour.global.log.logger.JsonLogger;
import kr.tour.global.log.property.api.ExceptionLogProperty;
import kr.tour.global.log.property.api.RequestLogProperty;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class RequestLoggingFallbackFilter extends OncePerRequestFilter {

    private static final List<String> excludedPaths = List.of(
            "/favicon.ico",
            "/actuator/**",
            "/swagger-ui/**", "/docs/**", "/webjars/**","/v3/api-docs/**","/.well-known/**"
    );
    private static final String LOGGED_BY_AOP = "loggedByAop";
    private static final String LOGGED_BY_FILTER = "loggedByFilter";
    private static final String TRUE = "true";

    private final JsonLogger jsonLogger;
    private final ConsoleLogger consoleLogger;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isExcludedPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException ex) {
            ExceptionLogProperty exceptionLogProperty =
                    ExceptionLogProperty.errorFromThrowable(
                            request.getRequestURI(),
                            request.getMethod(),
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            ex);
            jsonLogger.error(exceptionLogProperty);
            consoleLogger.error(exceptionLogProperty);
            throw ex;
        } finally {
            RequestLogProperty requestLogProperty = RequestLogProperty.forFilter(request);

            /*
            1. Dispatcher servlet 까지는 도달했으나, 예외가 발생했으며 @ControllerAdvice가 처리하지 않았고,
            Spring의 DefaultHandlerExceptionResolver가 예외를 던지지 않고 HTTP 상태 코드로 자동 변환하는 경우
            2. filter에서 try-catch로 잡지 못한 예외가 발생한 경우
             */
            if (!isLoggedByAop() && !isLoggedByFilter()) {
                jsonLogger.info(requestLogProperty);
                consoleLogger.info(requestLogProperty);

                logSpringGeneratedExceptionResponse(request, response);
            }
        }
    }

    private void logSpringGeneratedExceptionResponse(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        AutoResolvedErrorCode exceptionType = AutoResolvedErrorCode.from(status);
        ExceptionLogProperty exceptionLogProperty = ExceptionLogProperty.from(
                request.getRequestURI(),
                request.getMethod(),
                exceptionType.getHttpStatus(),
                exceptionType.getMessage());

        // 4xx는 warn, 5xx는 error로 로깅
        if (status.is4xxClientError()) {
            jsonLogger.warn(exceptionLogProperty);
            consoleLogger.warn(exceptionLogProperty);
        } else if (status.is5xxServerError()) {
            jsonLogger.error(exceptionLogProperty);
            consoleLogger.error(exceptionLogProperty);
        }
    }

    private boolean isLoggedByFilter() {
        return TRUE.equals(MDC.get(LOGGED_BY_FILTER));
    }

    private boolean isLoggedByAop() {
        return TRUE.equals(MDC.get(LOGGED_BY_AOP));
    }

    private boolean isExcludedPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        return excludedPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}

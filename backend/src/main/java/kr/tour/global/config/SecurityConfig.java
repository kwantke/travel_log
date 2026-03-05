package kr.tour.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.tour.global.auth.jwt.JwtAuthenticationFilter;
import kr.tour.global.dto.HttpRequestInfo;
import kr.tour.global.log.RequestLoggingFallbackFilter;
import kr.tour.global.log.logger.ConsoleLogger;
import kr.tour.global.log.logger.JsonLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

private static final List<HttpRequestInfo> whiteList= List.of(
        new HttpRequestInfo(HttpMethod.GET, "/actuator/**"),
        new HttpRequestInfo(HttpMethod.GET, "/h2-console/**"),
        new HttpRequestInfo(HttpMethod.POST, "/h2-console/**"),
        new HttpRequestInfo(HttpMethod.GET, "/favicon.ico"),
        new HttpRequestInfo(HttpMethod.GET, "/swagger-ui/**"),
        new HttpRequestInfo(HttpMethod.GET, "/swagger-resources/**"),
        new HttpRequestInfo(HttpMethod.GET, "/v3/api-docs/**"),
        new HttpRequestInfo(HttpMethod.GET, "/api/v1/travelogues/**"),
        new HttpRequestInfo(HttpMethod.POST, "/api/v1/login/**"),
        new HttpRequestInfo(HttpMethod.GET, "/api/v1/travel-plans/shared/**"),
        new HttpRequestInfo(HttpMethod.POST, "/api/v1/tags/**"),
        new HttpRequestInfo(HttpMethod.GET, "/api/v1/tags/**"),
        new HttpRequestInfo(HttpMethod.POST, "/api/v1/members"),
        new HttpRequestInfo(HttpMethod.OPTIONS, "/**")
);;

  private final JsonLogger jsonLogger;
  private final ConsoleLogger consoleLogger;
  private final ObjectMapper objectMapper;
  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{
    return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .securityMatcher("/api/**")
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
              whiteList.forEach(info ->
                      auth.requestMatchers(info.method(), info.urlPattern()).permitAll()
              );
              auth.anyRequest().authenticated();
            }).addFilterBefore(
                    new JwtAuthenticationFilter(
                            consoleLogger,
                            whiteList,
                            objectMapper,
                            jwtTokenProvider
                    ),
                    UsernamePasswordAuthenticationFilter.class
            ).build();

  }

  @Bean
  public RequestLoggingFallbackFilter requestLoggingFilter() {
    return new RequestLoggingFallbackFilter(jsonLogger, consoleLogger);
  }

  @Bean
  public FilterRegistrationBean<RequestLoggingFallbackFilter> requestLoggingFallbackFilterRegistration(
          RequestLoggingFallbackFilter requestLoggingFallbackFilter) {
    FilterRegistrationBean<RequestLoggingFallbackFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(requestLoggingFallbackFilter);
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(List.of("http://localhost:3000","http://3.38.95.119:80")); // 프론트 주소
    config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }
}

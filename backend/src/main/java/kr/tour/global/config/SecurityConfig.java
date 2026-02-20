package kr.tour.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.tour.auth.infrastructure.JwtTokenProvider;
import kr.tour.global.auth.jwt.JwtAuthenticationFilter;
import kr.tour.global.log.logger.ConsoleLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String[] PUBLIC_AUTH_ENDPOINTS = {
          "/api/*/login/**"
  };


  private final ConsoleLogger consoleLogger;
  private final ObjectMapper objectMapper;
  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{
    return http
            .csrf(AbstractHttpConfigurer::disable)
            .securityMatcher("/api/**")
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(PUBLIC_AUTH_ENDPOINTS).permitAll().anyRequest().authenticated()
            ).addFilterBefore(
                    new JwtAuthenticationFilter(
                            consoleLogger,
                            Arrays.stream(PUBLIC_AUTH_ENDPOINTS).toList(),
                            objectMapper,
                            jwtTokenProvider
                    ),
                    UsernamePasswordAuthenticationFilter.class
            ).build();

  }

}

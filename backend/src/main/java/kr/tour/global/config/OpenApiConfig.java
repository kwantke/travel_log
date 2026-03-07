package kr.tour.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

  @Value("${swagger.servers.local}")
  private String localServerUrl;

  @Value("${swagger.servers.dev}")
  private String devServerUrl;

  @Value("${swagger.servers.prod}")
  private String prodServerUrl;

  @Bean
  public OpenAPI openAPI() {
    String securitySchemeName = "BearerAuth";

    // 1. 서버 리스트 정의
    Server localServer = new Server();
    localServer.setUrl(localServerUrl);
    localServer.setDescription("로컬 환경 (Local)");

    Server devServer = new Server();
    devServer.setUrl(devServerUrl); // 실제 개발 서버 도메인으로 변경하세요
    devServer.setDescription("개발 환경 (Development)");

    Server prodServer = new Server();
    prodServer.setUrl(prodServerUrl); // 실제 운영 서버 도메인으로 변경하세요
    prodServer.setDescription("운영 환경 (Production)");

    return new OpenAPI()
            .info(new Info()
                    .title("Travelogue API")
                    .description("여행기 서비스 API 명세서")
                    .version("v1.0.0"))
            .servers(List.of(localServer, devServer, prodServer))
            // 1. 모든 API에 대해 기본적으로 Security 적용 (전역 설정)
           //.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            // 2. Security 구성 정의 (Bearer JWT 방식)
            .components(new Components()
                    .addSecuritySchemes(securitySchemeName,
                            new SecurityScheme()
                                    .name(securitySchemeName)
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")));
  }
}

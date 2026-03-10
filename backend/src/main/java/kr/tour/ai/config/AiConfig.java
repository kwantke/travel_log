package kr.tour.ai.config;

import kr.tour.global.properties.LlmProviderProperties;
import kr.tour.global.properties.LlmProviderProperties.GeminiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({LlmProviderProperties.class})
public class AiConfig {

  @Bean
  public GeminiProperties geminiProperties(LlmProviderProperties properties) {
    // LlmProviderProperties 내부에 있는 gemini 객체를 빈으로 등록
    return properties.getGemini();
  }
}

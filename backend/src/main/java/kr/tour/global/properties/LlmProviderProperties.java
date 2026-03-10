package kr.tour.global.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "llm")
public class LlmProviderProperties {

  @NotBlank
  private String activeProvider = "gemini";

  private GeminiProperties gemini = new GeminiProperties();


  @Getter
  @Setter
  public static class GeminiProperties {
    @NotBlank
    private String apiKey;
    private String model = "gemini-2.5-flash";
    private String baseUrl = "https://generativelanguage.googleapis.com/v1beta";
    private double temperature = 0.7;
    private int maxOutputTokens = 65536;
  }
}

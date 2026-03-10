package kr.tour.ai.infrastructure.llm.gemini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.tour.ai.exception.LlmApiException;
import kr.tour.ai.infrastructure.llm.LlmRequest;
import kr.tour.global.properties.LlmProviderProperties.GeminiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiRequestBuilder {

  private final ObjectMapper objectMapper;
  private final GeminiProperties properties;

  public String buildRequestBody(LlmRequest request, boolean jsonResponse) {
    try {
      ObjectNode root = objectMapper.createObjectNode();

      ArrayNode contents = root.putArray("contents");
      ObjectNode content = contents.addObject();
      ArrayNode parts = content.putArray("parts");
      parts.addObject().put("text", request.prompt());

      ObjectNode config = root.putObject("generationConfig");
      config.put("temperature", resolveTemperature(request));
      config.put("maxOutputTokens", resolveMaxOutputTokens(request));

      if (jsonResponse) {
        config.put("responseMimeType", "application/json");
      }

      return objectMapper.writeValueAsString(root);

    } catch (JsonProcessingException e) {
      throw new LlmApiException("Gemini 요청 생성 실패", e);
    }
  }

  private double resolveTemperature(LlmRequest request) {
    return request.temperature() != null
            ? request.temperature()
            : properties.getTemperature();
  }

  private int resolveMaxOutputTokens(LlmRequest request) {
    return request.maxOutputTokens() != null
            ? request.maxOutputTokens()
            : properties.getMaxOutputTokens();
  }
}

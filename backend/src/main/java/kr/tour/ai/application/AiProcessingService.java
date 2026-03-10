package kr.tour.ai.application;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import kr.tour.ai.dto.response.TravelogTagGenerateResponse;
import kr.tour.ai.infrastructure.json.JsonExtractor;
import kr.tour.ai.infrastructure.llm.LlmProvider;
import kr.tour.ai.infrastructure.llm.LlmRequest;
import kr.tour.ai.infrastructure.llm.LlmResponse;
import kr.tour.ai.prompt.PromptLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Slf4j
@Service
@RequiredArgsConstructor
public class AiProcessingService {

  private final LlmProvider llmProvider;
  private final PromptLoader promptLoader;
  private final ObjectMapper objectMapper;
  private final JsonExtractor jsonExtractor;

  public TravelogTagGenerateResponse generateTags(String content) {
    validateContent(content);

    String systemPrompt = promptLoader.getTravelTagGenerationPrompt();
    String prompt = systemPrompt.formatted(content);

    LlmResponse response = llmProvider.generate(LlmRequest.of(prompt));
    String cleanJson = jsonExtractor.extractJsonObject(response.getText());
    TravelogTagGenerateResponse result = parseResponse(cleanJson);
    List<String> normalizedTags = normalizeTags(result.tags());

    return new TravelogTagGenerateResponse(normalizedTags);
  }


  private void validateContent(String content) {
    if (!StringUtils.hasText(content)) {
      throw new IllegalArgumentException("여행기 내용은 비어 있을 수 없습니다.");
    }
  }

  private TravelogTagGenerateResponse parseResponse(String responseText) {
    try {
      return objectMapper.readValue(responseText, TravelogTagGenerateResponse.class);
    } catch (JsonProcessingException e) {
      log.error("OpenAI 태그 응답 JSON 파싱 실패. response={}", responseText, e);
      throw new IllegalStateException("태그 생성 응답 파싱에 실패했습니다.", e);
    }
  }

  private List<String> normalizeTags(List<String> tags) {
    if (tags == null || tags.isEmpty()) {
      return List.of();
    }

    Set<String> uniqueTags = new LinkedHashSet<>();

    for (String tag : tags) {
      if (!StringUtils.hasText(tag)) {
        continue;
      }

      String normalized = tag.trim();
      if (!normalized.isEmpty()) {
        uniqueTags.add(normalized);
      }
    }

    return uniqueTags.stream()
            .limit(10)
            .toList();
  }
}
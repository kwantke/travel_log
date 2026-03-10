package kr.tour.ai.infrastructure.llm;

import lombok.Builder;

@Builder
public record LlmRequest(
        String prompt,
        Double temperature,
        Integer maxOutputTokens
) {
  public static LlmRequest of(String prompt) {
    return LlmRequest.builder()
            .prompt(prompt)
            .build();
  }
}

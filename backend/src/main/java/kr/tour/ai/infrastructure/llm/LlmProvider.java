package kr.tour.ai.infrastructure.llm;

public interface LlmProvider {

  LlmResponse generate(LlmRequest request);
}

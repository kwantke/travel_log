package kr.tour.ai.infrastructure.llm.gemini;

import kr.tour.ai.exception.LlmApiException;
import kr.tour.ai.infrastructure.llm.LlmProvider;
import kr.tour.ai.infrastructure.llm.LlmResponse;
import kr.tour.ai.infrastructure.llm.LlmRequest;
import kr.tour.global.properties.LlmProviderProperties.GeminiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiLlmProvider implements LlmProvider {
  private static final String PROVIDER_NAME = "gemini";
  private static final String URL_TEMPLATE = "%s/models/%s:generateContent?key=%s";

  private final RestClient restClient;
  private final GeminiProperties properties;
  private final GeminiRequestBuilder requestBuilder;
  private final GeminiResponseParser responseParser;

  @Override
  public LlmResponse generate(LlmRequest request) {
    log.debug("Gemini API 호출: 프롬프트 길이={},", request.prompt().length());
    return executeRequest(request, true);
  }

  private LlmResponse executeRequest(LlmRequest request, boolean jsonResponse) {
    String url = buildUrl();
    String requestBody = requestBuilder.buildRequestBody(request, jsonResponse);

    try {
      long startTime = System.currentTimeMillis();

      String body = restClient.post()
              .uri(url)
              .contentType(MediaType.APPLICATION_JSON)
              .body(requestBody)
              .retrieve()
              .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                if (res.getStatusCode().value() == 429) {
                  throw new LlmApiException("Gemini API 호출 횟수 초과");
                }
                throw new LlmApiException("Gemini API 호출 실패: " + res.getStatusCode());
              })
              .body(String.class);

      long latencyMs = System.currentTimeMillis() - startTime;
      log.info("Gemini API 응답 수신: {}ms 소요", latencyMs);

      if (body == null) {
        throw new LlmApiException("Gemini API 응답 본문이 비어있습니다");
      }

      return responseParser.parse(body, latencyMs);
    } catch (LlmApiException e) {
      throw e;
    } catch (RestClientResponseException e) {
      log.error("Gemini API 호출 실패: {}", e.getStatusCode(), e);
      throw new LlmApiException("Gemini API 호출 실패: " + e.getMessage(), e);
    } catch (Exception e) {
      log.error("Gemini API 호출 실패", e);
      throw new LlmApiException("Gemini API 호출 실패: " + e.getMessage(), e);
    }

  }

  private String buildUrl() {
    return String.format(URL_TEMPLATE,
            properties.getBaseUrl(),
            properties.getModel(),
            properties.getApiKey());
  }

}

package kr.tour.ai.exception;

import kr.tour.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LlmApiErrorCode implements ErrorCode {

  AI_API_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "외부 AI 서비스 호출에 실패했습니다."),
  AI_RESPONSE_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 응답 파싱에 실패했습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public String getCode() {
    return null;
  }
}

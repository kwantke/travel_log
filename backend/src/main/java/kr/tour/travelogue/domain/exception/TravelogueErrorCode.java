package kr.tour.travelogue.domain.exception;

import kr.tour.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TravelogueErrorCode implements ErrorCode {
  INVALID_KEYWORD(HttpStatus.BAD_REQUEST, "존재하지 않는 검색 키워드 종류입니다."),

  UNKNOWN_COUNTRY_CODE(HttpStatus.NOT_FOUND, "존재하지 않는 국가 코드입니다."),

  // Tag
  INVALID_TAG(HttpStatus.BAD_REQUEST, "존재하지 않는 태그입니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }
}

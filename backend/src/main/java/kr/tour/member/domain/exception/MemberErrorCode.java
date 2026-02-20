package kr.tour.member.domain.exception;

import kr.tour.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {

  INVALID_MEMBER_INFO(HttpStatus.BAD_REQUEST, "잘못된 이메일 또는 비밀번호입니다."),
  ;
  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }

}

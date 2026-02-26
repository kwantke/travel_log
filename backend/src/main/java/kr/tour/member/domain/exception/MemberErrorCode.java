package kr.tour.member.domain.exception;

import kr.tour.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {

  INVALID_MEMBER_INFO(HttpStatus.BAD_REQUEST, "잘못된 이메일 또는 비밀번호입니다."),

  INVALID_KAKAO_LOGIN_INFO(HttpStatus.BAD_REQUEST,"잘못된 로그인 요청입니다. 인가코드를 확인해주세요"),
  INTERNAL_ERROR_KAKAO_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, "외부 서비스의 장애로 카카오로그인을 이용할 수 없습니다"),
  NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND,"존재하지 않는 사용자입니다."),
  ;
  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }

}

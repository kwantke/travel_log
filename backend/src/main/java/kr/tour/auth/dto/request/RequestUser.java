package kr.tour.auth.dto.request;

import lombok.Getter;



@Getter
public class RequestUser {
  private String memberId;

  public RequestUser(String memberId) {
    this.memberId = memberId;
  }

  private void validate(String memberId) {
    if (memberId == null || "".equals(memberId)) {
      throw new IllegalStateException("memberId는 null일 수 없습니다.");
    }
  }
}

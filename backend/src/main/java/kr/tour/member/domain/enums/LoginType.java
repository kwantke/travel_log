package kr.tour.member.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {
  NONE("local"),
  KAKAO("kakao"),
  ;

  private final String name;
}

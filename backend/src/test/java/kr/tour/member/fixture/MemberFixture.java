package kr.tour.member.fixture;

import kr.tour.member.domain.Member;
import kr.tour.member.domain.enums.LoginType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberFixture {
  KAKAO_MEMBER(1L, null, null, "https://dev.tour.kr/temporary/profile.png", "테스터",
          LoginType.KAKAO),
  ;

  private final Long socialId;
  private final String email;
  private final String password;
  private final String profileImageUrl;
  private final String nickname;
  private final LoginType loginType;

  public Member build() {
    if (loginType == LoginType.KAKAO) {
      return new Member(socialId, nickname, profileImageUrl, loginType);
    }
    return new Member(email, password, nickname, profileImageUrl, loginType);
  }
}

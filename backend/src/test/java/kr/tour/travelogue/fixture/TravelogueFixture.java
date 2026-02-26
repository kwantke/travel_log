package kr.tour.travelogue.fixture;


import kr.tour.member.domain.Member;
import kr.tour.member.fixture.MemberFixture;
import kr.tour.travelogue.domain.Travelogue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TravelogueFixture {
  TRAVELOGUE(MemberFixture.KAKAO_MEMBER.build(), "광안리 해수욕장 가자", "https://dev.tour.kr/temporary/busan_thumbnail.png"),
  ;
  private final Member author;
  private final String title;
  private final String thumbnail;

  public Travelogue get() {
    return new Travelogue(author, title, thumbnail);
  }

  public Travelogue create(Member author) {
    return new Travelogue(author, title, thumbnail);
  }
}

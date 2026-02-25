package kr.tour.travelogue.helper;

import kr.tour.global.fixture.DbHelper;
import kr.tour.member.domain.Member;
import kr.tour.member.fixture.MemberFixture;
import org.springframework.stereotype.Component;

@Component
public class TravelogueTestHelper extends DbHelper {

  public Member initKakaoMemberTestData() {
    Member member = MemberFixture.KAKAO_MEMBER.build();
    em.persist(member);
    return member;
  }


}

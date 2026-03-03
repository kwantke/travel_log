package kr.tour.travelogue.helper;

import kr.tour.global.fixture.DbHelper;
import kr.tour.member.domain.Member;
import kr.tour.member.fixture.MemberFixture;
import kr.tour.travelogue.domain.*;
import kr.tour.travelogue.fixture.TagFixture;
import kr.tour.travelogue.fixture.TravelogueDayFixture;
import kr.tour.travelogue.fixture.TravelogueFixture;
import kr.tour.travelogue.fixture.TraveloguePlaceFixture;
import org.springframework.stereotype.Component;

import static kr.tour.travelogue.fixture.TravelogueCountryFixture.TRAVELOGUE_COUNTRY;
import static kr.tour.travelogue.fixture.TraveloguePhotoFixture.TRAVELOGUE_PHOTO;

@Component
public class TravelogueTestHelper extends DbHelper {

  public Member initKakaoMemberTestData() {
    Member member = MemberFixture.KAKAO_MEMBER.build();
    em.persist(member);
    em.flush();
    return member;
  }

  public void initAllTravelogueTestData() {
    Member author = initKakaoMemberTestData();
    Travelogue travelogue = initTravelogueTestData(author);
    initTravelogueTestDataWithTag(author);
    persistTravelogueLike(travelogue, author);
  }

  public Travelogue initTravelogueTestData(Member author) {
    Travelogue travelogue = persistTravelogue(author);
    TravelogueDay day = persistTravelogueDay(travelogue);
    TraveloguePlace place = persistTraveloguePlace(day);
    persistTravelogueCountry(travelogue);
    persistTraveloguePhoto(place);
    return travelogue;
  }

  public Travelogue initTravelogueTestDataWithTag(Member author) {
    Travelogue travelogue = persistTravelogue(author);
    TravelogueDay day = persistTravelogueDay(travelogue);
    TraveloguePlace place = persistTraveloguePlace(day);
    persistTravelogueCountry(travelogue);
    persistTraveloguePhoto(place);
    persisTravelogueTag(travelogue, TagFixture.TAG_1.get());
    return travelogue;
  }

  private TravelogueLike persistTravelogueLike(Travelogue travelogue, Member liker) {
    TravelogueLike like = new TravelogueLike(travelogue, liker);
    travelogue.increaseLikeCount();

    em.persist(travelogue);
    em.flush();

    em.persist(like);
    em.flush();
    return like;
  }



  private void persisTravelogueTag(Travelogue travelogue, Tag tag) {
    Tag savedTag = initTagTestData(tag);

    em.persist(new TravelogueTag(travelogue, savedTag));
    em.flush();
  }


  public Tag initTagTestData(Tag tag) {
    em.persist(tag);
    em.flush();
    return tag;
  }
  private TraveloguePhoto persistTraveloguePhoto(TraveloguePlace place) {
    TraveloguePhoto photo = TRAVELOGUE_PHOTO.create(place);
    em.persist(photo);
    em.flush();
    return photo;
  }

  private TravelogueCountry persistTravelogueCountry(Travelogue travelogue) {
    TravelogueCountry travelogueCountry = TRAVELOGUE_COUNTRY.create(travelogue);
    em.persist(travelogueCountry);
    em.flush();
    return travelogueCountry;
  }

  private TraveloguePlace persistTraveloguePlace(TravelogueDay day) {
    TraveloguePlace place= TraveloguePlaceFixture.TRAVELOGUE_PLACE.create(day);
    em.persist(place);
    em.flush();
    return place;
  }

  private TravelogueDay persistTravelogueDay(Travelogue travelogue) {
    TravelogueDay day = TravelogueDayFixture.TRAVELOGUE_DAY.create(1, travelogue);
    em.persist(day);
    em.flush();
    return day;
  }


  private Travelogue persistTravelogue(Member author) {
    Travelogue travelogue = TravelogueFixture.TRAVELOGUE.create(author);
    em.persist(travelogue);
    em.flush();
    return travelogue;
  }

  public Travelogue initTravelogueTestDataWithLike(Member liker) {
    Travelogue travelogue = initTravelogueTestData(liker);
    persistTravelogueLike(travelogue, liker);
    return travelogue;
  }
}

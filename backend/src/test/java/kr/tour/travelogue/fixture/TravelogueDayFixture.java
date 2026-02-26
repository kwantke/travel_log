package kr.tour.travelogue.fixture;

import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueDay;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TravelogueDayFixture {

  TRAVELOGUE_DAY(1, TravelogueFixture.TRAVELOGUE.get()),
  ;

  private final int order;
  private final Travelogue travelogue;

  public TravelogueDay get() {
    return new TravelogueDay(order, travelogue);
  }

  public TravelogueDay create(int order, Travelogue travelogue) {
    return new TravelogueDay(order, travelogue);
  }
}

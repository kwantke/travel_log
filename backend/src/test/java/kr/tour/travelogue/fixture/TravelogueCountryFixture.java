package kr.tour.travelogue.fixture;

import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueCountry;
import kr.tour.travelogue.domain.enums.CountryCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TravelogueCountryFixture {
  TRAVELOGUE_COUNTRY(CountryCode.KR, 1L),
  ;

  private final CountryCode countryCode;
  private final Long count;

  public TravelogueCountry create(Travelogue travelogue) {
    return new TravelogueCountry(travelogue, countryCode, count.intValue());
  }
}

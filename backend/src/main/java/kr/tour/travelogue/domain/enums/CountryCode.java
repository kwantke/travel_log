package kr.tour.travelogue.domain.enums;

import kr.tour.global.exception.CoreException;
import kr.tour.travelogue.domain.exception.TravelogueErrorCode;

import java.util.Arrays;
import java.util.Set;

public enum CountryCode {

  NONE(Set.of()),
  KR(Set.of("대한민국","한국")),
  ;
  private final Set<String> names;

  CountryCode(Set<String> names) {
    this.names = names;
  }

  public static CountryCode findByName(String name) {
    return Arrays.stream(values())
            .filter(code -> code.names.contains(name))
            .findFirst()
            .orElse(NONE);
  }

  public static CountryCode from(String code) {
    try {
      return CountryCode.valueOf(code.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new CoreException(TravelogueErrorCode.UNKNOWN_COUNTRY_CODE);
    }
  }
}

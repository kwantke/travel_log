package kr.tour.travelogue.domain.search;

import kr.tour.global.exception.CoreException;
import kr.tour.travelogue.domain.exception.TravelogueErrorCode;

public enum SearchType {
  TITLE, AUTHOR, COUNTRY,;

  public static SearchType from(String searchType) {
    try {
      return SearchType.valueOf(searchType.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new CoreException(TravelogueErrorCode.INVALID_KEYWORD);
    }
  }
}

package kr.tour.travelogue.domain.search;


import kr.tour.travelogue.domain.enums.CountryCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchCondition {
  private final String keyword;
  private final SearchType searchType;

  public boolean isNoneCountry() {
    return searchType == SearchType.COUNTRY && CountryCode.findByName(keyword) == CountryCode.NONE;
  }

  public boolean isEmptyCondition() {
    return keyword == null && searchType == null;
  }
}

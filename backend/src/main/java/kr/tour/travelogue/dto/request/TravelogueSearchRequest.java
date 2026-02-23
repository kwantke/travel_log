package kr.tour.travelogue.dto.request;

import jakarta.validation.constraints.Size;
import kr.tour.travelogue.domain.search.SearchCondition;
import kr.tour.travelogue.domain.search.SearchType;

public record TravelogueSearchRequest(
        @Size(min = 2, message = "검색어는 2글자 이상어야 합니다.")
        String keyword,

        String searchType

) {
        public SearchCondition toSearchCondition() {
          return new SearchCondition(keyword, getSearchType());
        }

        private SearchType getSearchType() {
          if (this.searchType == null) {
                return null;
          }

                return SearchType.from(this.searchType);
        }
}

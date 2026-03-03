package kr.tour.travelogue.dto.request;

import jakarta.validation.constraints.Size;
import kr.tour.travelogue.domain.search.SearchCondition;
import kr.tour.travelogue.domain.search.SearchType;

public record TravelogueSearchRequest(

        String keyword,

        String searchType

) {
        public SearchCondition toSearchCondition() {
          return new SearchCondition(keyword, getSearchType());
        }

        private SearchType getSearchType() {
          if (this.searchType == null || this.searchType == "") {
                return null;
          }

                return SearchType.from(this.searchType);
        }
}

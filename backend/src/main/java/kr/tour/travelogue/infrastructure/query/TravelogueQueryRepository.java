package kr.tour.travelogue.infrastructure.query;

import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueFilterCondition;
import kr.tour.travelogue.domain.search.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TravelogueQueryRepository {
  Page<Travelogue> findAllByCondition(SearchCondition searchCondition, TravelogueFilterCondition filterCondition, Pageable pageable);
}

package kr.tour.travelogue.application;

import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueFilterCondition;
import kr.tour.travelogue.domain.search.SearchCondition;
import kr.tour.travelogue.dto.request.TravelogueFilterRequest;
import kr.tour.travelogue.dto.request.TravelogueSearchRequest;
import kr.tour.travelogue.dto.response.TravelogueSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TravelogueFacadeService {

  private final TravelogueService travelogueService;

  public Page<TravelogueSimpleResponse> findSimpleTravelogues(
          TravelogueFilterRequest filterRequest,
          TravelogueSearchRequest searchRequest,
          Pageable pageable
  ) {
    TravelogueFilterCondition filter = filterRequest.toFilterCondition();
    SearchCondition searchCondition = searchRequest.toSearchCondition();

    Page<Travelogue> travelogues = travelogueService.findAll(searchCondition, filter, pageable);

    return travelogues.map(TravelogueSimpleResponse::from);
  }


}

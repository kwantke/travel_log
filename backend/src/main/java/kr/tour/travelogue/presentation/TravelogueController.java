package kr.tour.travelogue.presentation;

import io.swagger.v3.oas.annotations.Parameter;
import kr.tour.travelogue.application.TravelogueFacadeService;
import kr.tour.travelogue.dto.request.TravelogueFilterRequest;
import kr.tour.travelogue.dto.request.TravelogueSearchRequest;
import kr.tour.travelogue.dto.response.TravelogueSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/travelogues")
public class TravelogueController {

  private final TravelogueFacadeService travelogueFacadeService;

  @GetMapping
  public ResponseEntity<Page<TravelogueSimpleResponse>> findMainPageTravelogues(
          @Parameter(hidden = true)
          @PageableDefault(size=5, sort = "id", direction = Sort.Direction.DESC)
          Pageable pageable,
          TravelogueFilterRequest filterRequest,
          TravelogueSearchRequest searchRequest
  ) {

    Page<TravelogueSimpleResponse> data = travelogueFacadeService.findSimpleTravelogues(
            filterRequest,
            searchRequest,
            pageable
    );

    return ResponseEntity.ok(data);
  }
}

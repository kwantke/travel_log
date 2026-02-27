package kr.tour.travelogue.presentation;


import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import kr.tour.global.dto.MemberAuth;
import kr.tour.travelogue.application.TravelogueFacadeService;
import kr.tour.travelogue.dto.request.TravelogueFilterRequest;
import kr.tour.travelogue.dto.request.TravelogueRequest;
import kr.tour.travelogue.dto.request.TravelogueSearchRequest;
import kr.tour.travelogue.dto.response.TravelogueCreateResponse;
import kr.tour.travelogue.dto.response.TravelogueLikeResponse;
import kr.tour.travelogue.dto.response.TravelogueResponse;
import kr.tour.travelogue.dto.response.TravelogueSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

  @PostMapping
  public ResponseEntity<Void> createTravelogue(
          @Valid @AuthenticationPrincipal MemberAuth member,
          @Valid @RequestBody TravelogueRequest request
          ) {
    TravelogueCreateResponse response = travelogueFacadeService.createTravelogue(member, request);
    return ResponseEntity.created(URI.create("/api/v1/travelogues/" + response.id())).build();
  }


  @GetMapping("/{id}")
  public ResponseEntity<TravelogueResponse> findTravelogue(
          @PathVariable Long id
  ) {
    return ResponseEntity.ok(travelogueFacadeService.findTravelogueByIdForGuest(id));
  }

  @GetMapping(value = "/{id}", headers = {HttpHeaders.AUTHORIZATION})
  public ResponseEntity<TravelogueResponse> findTravelogue(
          @PathVariable Long id,
          @Valid @AuthenticationPrincipal MemberAuth member){
    return ResponseEntity.ok(travelogueFacadeService.findTravelogueByIdForAuthenticated(id, member));
  }

  @PostMapping("/{id}/like")
  public ResponseEntity<TravelogueLikeResponse> likeTravelogue(@PathVariable Long id, @Valid MemberAuth member) {
    return ResponseEntity.ok()
            .body(travelogueFacadeService.likeTravelogue(id, member));
  }

}

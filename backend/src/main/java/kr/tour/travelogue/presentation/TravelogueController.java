package kr.tour.travelogue.presentation;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kr.tour.global.dto.MemberAuth;
import kr.tour.global.exception.ExceptionResponse;
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

  @Operation(summary = "여행기 메인 페이지 조회")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "요청이 정상적으로 처리되었을 때"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "올바르지 않은 페이지네이션 옵션으로 요청했을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          )
  })
  @GetMapping
  public ResponseEntity<Page<TravelogueSimpleResponse>> findMainPageTravelogues(
          @Parameter(hidden = true)
          @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)
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

  @Operation(summary = "여행기 작성")
  @SecurityRequirement(name = "BearerAuth")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "201",
                  description = "요청이 정상적으로 처리되었을 때"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "요청 Body에 올바르지 않은 값이 전달되었을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          ),
  })
  @PostMapping
  public ResponseEntity<Void> createTravelogue(
          @Valid @AuthenticationPrincipal MemberAuth member,
          @Valid @RequestBody TravelogueRequest request
          ) {
    TravelogueCreateResponse response = travelogueFacadeService.createTravelogue(member, request);
    return ResponseEntity.created(URI.create("/api/v1/travelogues/" + response.id())).build();
  }

  @Operation(summary = "여행기 상세 조회 - 비회원")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "요청이 정상적으로 처리되었을 때"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "존재하지 않는 여행기 ID로 요청했을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          ),
  })
  @GetMapping("/{id}")
  public ResponseEntity<TravelogueResponse> findTravelogue(
          @PathVariable Long id
  ) {
    return ResponseEntity.ok(travelogueFacadeService.findTravelogueByIdForGuest(id));
  }

  @Operation(summary = "여행기 상세 조회 - 회원")
  @SecurityRequirement(name = "BearerAuth")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "요청이 정상적으로 처리되었을 때"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "존재하지 않는 여행기 ID로 요청했을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          ),
  })
  @GetMapping(value = "/{id}", headers = {HttpHeaders.AUTHORIZATION})
  public ResponseEntity<TravelogueResponse> findTravelogue(
          @PathVariable Long id,
          @Valid @AuthenticationPrincipal MemberAuth member){
    return ResponseEntity.ok(travelogueFacadeService.findTravelogueByIdForAuthenticated(id, member));
  }

  @Operation(summary = "여행기 좋아요")
  @SecurityRequirement(name = "BearerAuth")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "201",
                  description = "요청이 정상적으로 처리되었을 때"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "존재하지 않는 여행기 ID로 요청했을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          ),
          @ApiResponse(
                  responseCode = "401",
                  description = "로그인하지 않은 사용자가 좋아요를 할 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          ),
  })
  @PostMapping("/{id}/like")
  public ResponseEntity<TravelogueLikeResponse> likeTravelogue(
          @PathVariable Long id,
          @Valid @AuthenticationPrincipal MemberAuth member) {
    return ResponseEntity.ok()
            .body(travelogueFacadeService.likeTravelogue(id, member));
  }

  @Operation(summary = "여행기 수정")
  @SecurityRequirement(name = "BearerAuth")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "요청이 정상적으로 처리되었을 때"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "요청 Body에 올바르지 않은 값이 전달되었을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          ),
          @ApiResponse(
                  responseCode = "403",
                  description = "작성자가 아닌 사용자가 요청했을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          )
  })
  @PutMapping("{id}")
  public ResponseEntity<Void> updateTravelogue(
          @PathVariable Long id,
          @Valid @AuthenticationPrincipal MemberAuth member,
          @Valid @RequestBody TravelogueRequest request
  ) {
    travelogueFacadeService.updateTravelogue(id, member, request);
    return ResponseEntity.ok().build();

  }

  @Operation(summary = "여행기 삭제")
  @SecurityRequirement(name = "BearerAuth")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "204",
                  description = "요청이 정상적으로 처리되었을 때"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "존재하지 않는 여행기 ID로 요청했을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          ),
          @ApiResponse(
                  responseCode = "403",
                  description = "작성자가 아닌 사용자가 요청했을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          )
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTravelogue(
          @PathVariable Long id,
          @Valid @AuthenticationPrincipal MemberAuth memberAuth
  ) {
    travelogueFacadeService.deleteTravelogueById(id, memberAuth);
    return ResponseEntity.noContent()
            .build();
  }

  @Operation(summary = "여행기 좋아요 취소")
  @SecurityRequirement(name = "BearerAuth")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "요청이 정상적으로 처리되었을 때"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "존재하지 않는 여행기 ID로 요청했을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          ),
          @ApiResponse(
                  responseCode = "401",
                  description = "로그인하지 않은 사용자가 좋아요를 취소 할 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          ),
  })
  @DeleteMapping("/{id}/like")
  public ResponseEntity<TravelogueLikeResponse> unlikeTravelogue(
          @PathVariable Long id,
          @Valid @AuthenticationPrincipal MemberAuth member
  ) {
    return ResponseEntity.ok()
            .body(travelogueFacadeService.unlikeTravelogue(id, member));
  }

}

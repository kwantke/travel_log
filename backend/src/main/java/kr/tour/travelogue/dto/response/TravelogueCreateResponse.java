package kr.tour.travelogue.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.tour.travelogue.domain.Travelogue;
import lombok.Builder;

@Builder
public record TravelogueCreateResponse(
        @Schema(description = "생성된 여행기 id", example = "1")
        Long id
) {
  public static TravelogueCreateResponse from(Travelogue travelogue) {
    return new TravelogueCreateResponse(travelogue.getId());
  }
}

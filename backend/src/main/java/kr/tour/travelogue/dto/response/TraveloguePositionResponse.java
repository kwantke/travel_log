package kr.tour.travelogue.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.tour.travelogue.domain.vo.Position;
import lombok.Builder;

@Builder
public record TraveloguePositionResponse(
        @Schema(description = "여행기 장소 위도", example = "37.5175896")
        String lat,
        @Schema(description = "여행기 장소 설명", example = "127.0867236")
        String lng
) {

  public static TraveloguePositionResponse from(Position position) {
    return TraveloguePositionResponse.builder()
            .lat(position.getLatitude())
            .lng(position.getLongitude())
            .build();
  }
}

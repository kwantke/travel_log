package kr.tour.travelogue.dto.response;

import kr.tour.travelogue.domain.Travelogue;
import lombok.Builder;

@Builder
public record TravelogueCreateResponse(
        Long id
) {
  public static TravelogueCreateResponse from(Travelogue travelogue) {
    return new TravelogueCreateResponse(travelogue.getId());
  }
}

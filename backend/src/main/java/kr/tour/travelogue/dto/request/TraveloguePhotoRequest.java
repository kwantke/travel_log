package kr.tour.travelogue.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.tour.travelogue.domain.TraveloguePhoto;
import kr.tour.travelogue.domain.TraveloguePlace;

public record TraveloguePhotoRequest(
        @Schema(description = "여행 사진", example = "https://dev.tour.kr/temporary/image9.png")
        String url
) {
  public TraveloguePhoto toTraveloguePhoto(int photoOrder, TraveloguePlace traveloguePlace) {
    TraveloguePhoto traveloguePhoto = new TraveloguePhoto(photoOrder, url, traveloguePlace);
    return traveloguePhoto;
  }
}

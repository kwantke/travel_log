package kr.tour.travelogue.dto.request;

import kr.tour.travelogue.domain.TraveloguePhoto;
import kr.tour.travelogue.domain.TraveloguePlace;

public record TraveloguePhotoRequest(
        String url
) {
  public TraveloguePhoto toTraveloguePhoto(int photoOrder, TraveloguePlace traveloguePlace) {
    TraveloguePhoto traveloguePhoto = new TraveloguePhoto(photoOrder, url, traveloguePlace);
    return traveloguePhoto;
  }
}

package kr.tour.travelogue.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.tour.travelogue.domain.TravelogueDay;
import kr.tour.travelogue.domain.TraveloguePhoto;
import kr.tour.travelogue.domain.TraveloguePlace;

import java.util.List;

public record TraveloguePlaceRequest(
        @NotBlank(message = "여행기 장소 이름은 비어있을 수 없습니다.")
        @Size(message = "장소 이름은 60자를 초과할 수 없습니다", max = 60)
        String placeName,

        @NotNull(message = "여행기 장소 위치 정보는 비어 있을 수 없습니다.")
        TraveloguePositionRequest position,

        @Size(message = "장소 설명은 300글자 이하입니다.")
        String description,

        @Size(message = "여행기 장소 사진은 쵀대 10개입니다.", max = 10)
        List<TraveloguePhotoRequest> photoUrls,
        String countryCode

) {
        public TraveloguePlace toTraveloguePlace(int order, TravelogueDay travelogueDay) {
                TraveloguePlace traveloguePlace = new TraveloguePlace(
                        order,
                        description,
                        placeName,
                        position().lat(),
                        position().lng(),
                        travelogueDay,
                        countryCode
                );
                addTraveloguePhotos(traveloguePlace);
                return traveloguePlace;
        }

        private void addTraveloguePhotos(TraveloguePlace traveloguePlace) {
                for (int photoOrder = 0; photoOrder < photoUrls.size(); photoOrder++) {
                        TraveloguePhotoRequest photoRequest = photoUrls.get(0);
                        TraveloguePhoto traveloguePhoto = photoRequest.toTraveloguePhoto(photoOrder, traveloguePlace);
                        traveloguePlace.addPhoto(traveloguePhoto);
                }

        }

}

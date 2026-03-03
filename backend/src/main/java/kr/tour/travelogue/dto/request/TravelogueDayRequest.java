package kr.tour.travelogue.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueDay;
import kr.tour.travelogue.domain.TraveloguePlace;

import java.util.List;

public record TravelogueDayRequest(
        @Schema(description = "여행기 장소 목록")
        @NotNull(message = "여행기 장소 목록은 비어있을 수 없습니다.")
        @Size(message = "여행기 장소는 최소 한 곳은 포함되어야 합니다.", min = 1)
        @Valid
        List<TraveloguePlaceRequest> places
){

        public TravelogueDay toTravelogueDay(int order, Travelogue travelogue) {
                TravelogueDay travelogueDay = new TravelogueDay(order, travelogue);
                addTraveloguePlaces(travelogueDay);
                return travelogueDay;
        }

        private void addTraveloguePlaces(TravelogueDay travelogueDay) {
                for (int placeOrder = 0; placeOrder < places.size(); placeOrder++) {
                        TraveloguePlaceRequest placeRequest = places.get(placeOrder);
                        TraveloguePlace traveloguePlace = placeRequest.toTraveloguePlace(placeOrder, travelogueDay);
                        travelogueDay.addPlace(traveloguePlace);
                }
        }
}

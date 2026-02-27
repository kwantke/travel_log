package kr.tour.travelogue.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.tour.travelogue.domain.TravelogueDay;
import kr.tour.travelogue.domain.TraveloguePlace;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelogueDayResponse(
        @Schema(description = "여행기 일자 ID", example = "1")
        Long id,
        @Schema(description = "여행기 장소 목록")
        List<TraveloguePlaceResponse> places
) {
        public static TravelogueDayResponse from(TravelogueDay travelogueDay) {
                return TravelogueDayResponse.builder()
                        .id(travelogueDay.getId())
                        .places(getTraveloguePlaceResponse(travelogueDay.getTraveloguePlaces()))
                        .build();
        }

        private static List<TraveloguePlaceResponse> getTraveloguePlaceResponse(List<TraveloguePlace> traveloguePlaces) {
                return traveloguePlaces.stream()
                        .map(TraveloguePlaceResponse::from)
                        .toList();
        }
}

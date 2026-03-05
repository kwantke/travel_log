package kr.tour.travelogue.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.tour.member.domain.Member;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueDay;

import java.util.ArrayList;
import java.util.List;

public record TravelogueRequest(
        @Schema(description = "여행기 제목", example = "서울 강남 여행기")
        @NotBlank(message = "여행기 제목은 비어있을 수 없습니다.")
        @Size(message = "여행기 제목은 20자를 초과할 수 없습니다.", max = 20)
        String title,
        @Schema(description = "여행기 썸네일", example = "https://dev.tour.kr/images/thumbnail.png")
        @NotBlank(message = "여행기 썸네일은 비어있을 수 없습니다.")
        String thumbnail,
        @Schema(description = "선택된 여행기 태그의 id 목록", example = "[1, 2, 3]")
        @NotNull(message = "여행기 태그 필드는 비어있을 수 없습니다.")
        @Size(max = 5, message = "여행기 태그는 최대 5개까지 입력할 수 있습니다.")
        List<Long> tags,
        @Schema(description = "여행기 일자 목록")
        @NotNull(message = "여행기 일자 목록은 비어있을 수 없습니다.")
        @Size(message = "여행기 일자는 최소 1일은 포함되어야 합니다.", min = 1)
        @Valid
        List<TravelogueDayRequest> days
){
  public Travelogue toTravelogue(Member author) {
    Travelogue travelogue = new Travelogue(author,title,thumbnail);
    addTravelogueDays(travelogue);
    return travelogue;

  }

  private void addTravelogueDays(Travelogue travelogue) {
    for (int dayOrder = 0; dayOrder < days.size(); dayOrder++) {
      TravelogueDayRequest dayRequest = days.get(dayOrder);
      TravelogueDay travelogueDay = dayRequest.toTravelogueDay(dayOrder, travelogue);
      travelogue.addDay(travelogueDay);
    }
  }

  public List<TravelogueDay> getTravelogueDays(Travelogue travelogue) {
    List<TravelogueDay> travelogueDays = new ArrayList<>();
    for (int dayOrder = 0; dayOrder < days.size(); dayOrder++) {
      TravelogueDayRequest dayRequest = days.get(dayOrder);
      TravelogueDay travelogueDay = dayRequest.toTravelogueDay(dayOrder, travelogue);
      travelogueDays.add(travelogueDay);
    }
    return travelogueDays;
  }
}

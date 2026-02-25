package kr.tour.travelogue.dto.request;

import kr.tour.member.domain.Member;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueDay;

import java.util.List;

public record TravelogueRequest(
        String title,
        String thumbnail,
        List<Long> tags,
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

}

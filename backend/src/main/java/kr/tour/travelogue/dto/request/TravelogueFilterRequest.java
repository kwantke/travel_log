package kr.tour.travelogue.dto.request;

import kr.tour.travelogue.domain.TravelogueFilterCondition;

import java.util.List;

public record TravelogueFilterRequest(
        List<Long> tag,
        Integer period) {

  public TravelogueFilterCondition toFilterCondition() {
    return new TravelogueFilterCondition(tag, period);
  }
}

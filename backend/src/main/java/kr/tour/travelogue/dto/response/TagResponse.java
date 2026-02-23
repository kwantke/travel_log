package kr.tour.travelogue.dto.response;

import kr.tour.travelogue.domain.Tag;
import kr.tour.travelogue.domain.TravelogueTag;
import lombok.Builder;

@Builder
public record TagResponse(
        Long id,
        String tag
) {
  public static TagResponse from(Tag tag) {
    return TagResponse.builder()
            .id(tag.getId())
            .tag(tag.getTag())
            .build();
  }


  public static TagResponse from(TravelogueTag travelogueTag) {
    return TagResponse.builder()
            .id(travelogueTag.getId())
            .tag(travelogueTag.getTag().getTag())
            .build();
  }
}

package kr.tour.travelogue.dto.response;

import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueTag;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelogueSimpleResponse(

        Long id,
        String title,
        String thumbnail,
        String authorNickname,
        String authorProfileUrl,
        List<TagResponse> tags,
        Long likeCount
) {

  public static TravelogueSimpleResponse from(Travelogue travelogue) {
    return TravelogueSimpleResponse.builder()
            .id(travelogue.getId())
            .title(travelogue.getTitle())
            .thumbnail(travelogue.getThumbnail())
            .authorNickname(travelogue.getAuthor().getNickname())
            .authorProfileUrl(travelogue.getAuthor().getProfileImageUrl())
            .tags(getTravelogueTags(travelogue.getTravelogueTags()))
            .likeCount(travelogue.getLikeCount())
            .build();
  }

  private static List<TagResponse> getTravelogueTags(List<TravelogueTag> travelogueTags) {
    return travelogueTags.stream()
            .map(TagResponse::from).toList();
  }
}

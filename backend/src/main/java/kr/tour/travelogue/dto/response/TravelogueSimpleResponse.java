package kr.tour.travelogue.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueTag;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelogueSimpleResponse(

        @Schema(description = "여행기 ID", example = "1")
        Long id,
        @Schema(description = "여행기 제목", example = "서울 강남 여행기")
        String title,
        @Schema(description = "여행기 썸네일 링크", example = "https://dev.touroot.kr/images/thumbnail.png")
        String thumbnail,
        @Schema(description = "작성자 닉네임", example = "지니")
        String authorNickname,
        @Schema(description = "작성자 프로필 사진 URL", example = "https://dev.touroot.kr/images/profile.png")
        String authorProfileUrl,
        @Schema(description = "여행기 태그 목록")
        List<TagResponse> tags,
        @Schema(description = "작성자 프로필 사진 URL", example = "10")
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

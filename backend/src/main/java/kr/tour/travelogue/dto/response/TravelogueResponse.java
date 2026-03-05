package kr.tour.travelogue.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueDay;
import kr.tour.travelogue.domain.TravelogueTag;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TravelogueResponse(
        @Schema(description = "여행기 ID", example = "1")
        Long id,
        @Schema(description = "여행기 제목", example = "서울 강남 여행기")
        String title,
        @Schema(description = "작성자 ID", example = "1")
        Long authorId,
        @Schema(description = "작성자 닉네임", example = "지니")
        String authorNickname,
        @Schema(description = "작성자 프로필 사진 URL", example = "https://dev.tour.kr/images/profile.png")
        String authorProfileImageUrl,
        @Schema(description = "여행기 썸네일 링크", example = "https://dev.tour.kr/images/thumbnail.png")
        String thumbnail,
        @Schema(description = "작성 날짜")
        LocalDate createdAt,
        @Schema(description = "여행기 태그")
        List<TagResponse> tags,
        @Schema(description = "여행기 일자 목록")
        List<TravelogueDayResponse> days,

        @Schema(description = "여행기 좋아요 숫자", example = "10")
        Long likeCount,
        @Schema(description = "사용자의 여행기 좋아요 여부", example = "true")
        Boolean isLiked
) {

  public static TravelogueResponse of(Travelogue travelogue, List<TravelogueTag> tags, boolean isLikedFromAccessor) {
    return baseBuilder(travelogue, tags)
            .isLiked(isLikedFromAccessor)
            .build();
  }

  public static TravelogueResponse createResponseForGuest(Travelogue travelogue, List<TravelogueTag> tags) {
    return baseBuilder(travelogue, tags)
            .isLiked(false)
            .build();
  }

  private static TravelogueResponseBuilder baseBuilder(
          Travelogue travelogue,
          List<TravelogueTag> tags
  ) {
    return TravelogueResponse.builder()
            .id(travelogue.getId())
            .createdAt(travelogue.getCreatedAt().toLocalDate())
            .authorId(travelogue.getAuthorId())
            .authorNickname(travelogue.getAuthorNickname())
            .authorProfileImageUrl(travelogue.getAuthorProfileImageUrl())
            .title(travelogue.getTitle())
            .thumbnail(travelogue.getThumbnail())
            .tags(getTagResponse(tags))
            .days(getTravelogueDayResponse(travelogue.getTravelogueDays()))
            .likeCount(travelogue.getLikeCount());
  }

  private static List<TravelogueDayResponse> getTravelogueDayResponse(List<TravelogueDay> travelogueDays) {
    return travelogueDays.stream()
            .map(TravelogueDayResponse::from)
            .toList();
  }

  private static List<TagResponse> getTagResponse(List<TravelogueTag> travelogueTags) {
    return travelogueTags.stream()
            .map(TagResponse::from)
            .toList();
  }
}


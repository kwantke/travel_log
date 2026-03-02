package kr.tour.travelogue.fixture;

import kr.tour.travelogue.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component

public class TravelogueResponseFixture {

  private TravelogueResponseFixture() {
  }

  public static Page<TravelogueSimpleResponse> getTravelogueSimpleResponses() {
    List<TravelogueSimpleResponse> responses = List.of(
            TravelogueSimpleResponse.builder()
                    .id(2L)
                    .title("광안리 해수욕장 가자")
                    .authorNickname("테스터")
                    .authorProfileUrl("https://dev.tour.kr/temporary/profile.png")
                    .thumbnail("https://dev.tour.kr/temporary/busan_thumbnail.png")
                    .tags(List.of(TagFixture.TAG_1.getResponse(1L)))
                    .likeCount(0L)
                    .build(),
            TravelogueSimpleResponse.builder()
                    .id(1L)
                    .title("광안리 해수욕장 가자")
                    .authorNickname("테스터")
                    .authorProfileUrl("https://dev.tour.kr/temporary/profile.png")
                    .thumbnail("https://dev.tour.kr/temporary/busan_thumbnail.png")
                    .tags(List.of())
                    .likeCount(1L)
                    .build()
    );

    return new PageImpl<>(responses, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id")), responses.size());
  }

  public static TravelogueResponse getTravelogueResponse() {
    return TravelogueResponse.builder()
            .id(1L)
            .title("광안리 해수욕장 가자")
            .createdAt(LocalDate.now())
            .authorNickname("테스터")
            .authorId(1L)
            .authorProfileImageUrl("https://dev.tour.kr/temporary/profile.png")
            .thumbnail("https://dev.tour.kr/temporary/busan_thumbnail.png")
            .days(getTravelogueDayResponses())
            .tags(List.of())
            .isLiked(false)
            .likeCount(0L)
            .build();
  }

  public static List<TravelogueDayResponse> getTravelogueDayResponses() {
    return List.of(TravelogueDayResponse.builder()
            .id(1L)
            .places(getTraveloguePlaceResponses())
            .build()
    );
  }

  public static List<TraveloguePlaceResponse> getTraveloguePlaceResponses() {
    return List.of(TraveloguePlaceResponse.builder()
            .id(1L)
            .placeName("광안리 해수욕장")
            .description("에메랄드 빛 해변")
            .position(getTraveloguePositionResponse())
            .photoUrls(getTraveloguePhotoUrls())
            .countryCode("KR")
            .build()
    );
  }

  public static TraveloguePositionResponse getTraveloguePositionResponse() {
    return TraveloguePositionResponse.builder()
            .lat("34.54343")
            .lng("126.66977")
            .build();
  };

  public static List<String> getTraveloguePhotoUrls() {
    return List.of("https://dev.tour.kr/temporary/image1.png");
  }

  public static TravelogueResponse getTravelogueResponseWithLike() {
    return TravelogueResponse.builder()
            .id(1L)
            .title("광안리 해수욕장 가자")
            .createdAt(LocalDate.now())
            .authorNickname("테스터")
            .authorId(1L)
            .authorProfileImageUrl("https://dev.tour.kr/temporary/profile.png")
            .thumbnail("https://dev.tour.kr/temporary/busan_thumbnail.png")
            .days(getTravelogueDayResponses())
            .tags(List.of())
            .isLiked(true)
            .likeCount(1L)
            .build();
  }

  public static TravelogueResponse getTravelogueResponseWithTag() {
    return TravelogueResponse.builder()
            .id(1L)
            .title("광안리 해수욕장 가자")
            .createdAt(LocalDate.now())
            .authorNickname("테스터")
            .authorId(1L)
            .authorProfileImageUrl("https://dev.tour.kr/temporary/profile.png")
            .thumbnail("https://dev.tour.kr/temporary/busan_thumbnail.png")
            .days(getTravelogueDayResponses())
            .tags(List.of(TagFixture.TAG_1.getResponse(1L)))
            .isLiked(false)
            .likeCount(0L)
            .build();
  }

  public static Page<TravelogueSimpleResponse> getTravelogueSimpleResponsesOrderByLikeCount() {
    List<TravelogueSimpleResponse> responses = List.of(
            TravelogueSimpleResponse.builder()
                    .id(1L)
                    .title("광안리 해수욕장 가자")
                    .authorNickname("테스터")
                    .authorProfileUrl("https://dev.tour.kr/temporary/profile.png")
                    .thumbnail("https://dev.tour.kr/temporary/busan_thumbnail.png")
                    .tags(List.of())
                    .likeCount(1L)
                    .build(),
            TravelogueSimpleResponse.builder()
                    .id(2L)
                    .title("광안리 해수욕장 가자")
                    .authorNickname("테스터")
                    .authorProfileUrl("https://dev.tour.kr/temporary/profile.png")
                    .thumbnail("https://dev.tour.kr/temporary/busan_thumbnail.png")
                    .tags(List.of(TagFixture.TAG_1.getResponse(1L)))
                    .likeCount(0L)
                    .build()
    );
    return new PageImpl<>(responses, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id")), responses.size());
  }
}

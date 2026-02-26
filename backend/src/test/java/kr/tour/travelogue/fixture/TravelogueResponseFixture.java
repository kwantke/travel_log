package kr.tour.travelogue.fixture;

import kr.tour.travelogue.dto.response.TravelogueSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

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
}

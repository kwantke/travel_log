package kr.tour.travelogue.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.tour.global.DatabaseCleaner;
import kr.tour.global.config.JwtTokenProvider;
import kr.tour.global.fixture.IntegrationTest;
import kr.tour.image.infrastructure.AwsS3Provider;
import kr.tour.member.domain.Member;
import kr.tour.travelogue.dto.request.TravelogueDayRequest;
import kr.tour.travelogue.dto.request.TraveloguePhotoRequest;
import kr.tour.travelogue.dto.request.TraveloguePlaceRequest;
import kr.tour.travelogue.dto.request.TravelogueRequest;
import kr.tour.travelogue.fixture.TravelogueRequestFixture;
import kr.tour.travelogue.helper.TravelogueTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("여행기 컨트롤러 통합 테스트")
public class TravelogueIntegrationTest extends IntegrationTest {

  @Autowired
  private DatabaseCleaner databaseCleaner;
  @Autowired
  private TravelogueTestHelper testHelper;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  @MockitoBean
  private AwsS3Provider s3Provider;

  private Member member;
  private String accessToken;
  @BeforeEach
  void setUp() {

    databaseCleaner.executeTruncate();

    member = testHelper.initKakaoMemberTestData();
    accessToken = jwtTokenProvider.createToken(member.getId())
            .accessToken();
  }

  @DisplayName("태그가 없는 여행기를 작성한다.")
  @Test
  void createTravelogue() {
    given(s3Provider.copyImageToPermanentStorage(any(String.class)))
            .willReturn("https://dev.touroot.kr/image.png");

    List<TravelogueDayRequest> days = getTravelogueDayRequests();
    TravelogueRequest request = TravelogueRequestFixture.getTravelogueRequest(days);

    RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .body(request)
            .when().post("/api/v1/travelogues")
            .then().log().all()
            .statusCode(201)
            .header("Location", "/api/v1/travelogues/1");
  }
  private List<TravelogueDayRequest> getTravelogueDayRequests() {
    List<TraveloguePhotoRequest> photos = TravelogueRequestFixture.getTraveloguePhotoRequests();
    List<TraveloguePlaceRequest> places = TravelogueRequestFixture.getTraveloguePlaceRequests(photos);
    return TravelogueRequestFixture.getTravelogueDayRequests(places);
  }
}

package kr.tour.travelogue.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.tour.global.DatabaseCleaner;
import kr.tour.global.config.JwtTokenProvider;
import kr.tour.global.exception.ExceptionResponse;
import kr.tour.global.fixture.IntegrationTest;
import kr.tour.image.infrastructure.AwsS3Provider;
import kr.tour.member.domain.Member;
import kr.tour.travelogue.dto.request.TravelogueDayRequest;
import kr.tour.travelogue.dto.request.TraveloguePhotoRequest;
import kr.tour.travelogue.dto.request.TraveloguePlaceRequest;
import kr.tour.travelogue.dto.request.TravelogueRequest;
import kr.tour.travelogue.dto.response.TravelogueLikeResponse;
import kr.tour.travelogue.dto.response.TravelogueResponse;
import kr.tour.travelogue.dto.response.TravelogueSimpleResponse;
import kr.tour.travelogue.fixture.TagFixture;
import kr.tour.travelogue.fixture.TravelogueRequestFixture;
import kr.tour.travelogue.fixture.TravelogueResponseFixture;
import kr.tour.travelogue.helper.TravelogueTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.util.List;

import static org.hamcrest.Matchers.is;
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

  @DisplayName("메인페이지 조회 시, 최신 작성 순으로 여행기를 조회한다.")
  @Test
  void findMainPageTravelogues() throws JsonProcessingException {
    testHelper.initAllTravelogueTestData();
    Page<TravelogueSimpleResponse> responses = TravelogueResponseFixture.getTravelogueSimpleResponses();

    RestAssured.given().log().all()
            .accept(ContentType.JSON)
            .when().get("/api/v1/travelogues")
            .then().log().all()
            .statusCode(200).assertThat()
            .body(is(objectMapper.writeValueAsString(responses)));
  }

  @DisplayName("태그가 없는 여행기를 작성한다.")
  @Test
  void createTravelogue() {
    given(s3Provider.copyImageToPermanentStorage(any(String.class)))
            .willReturn("https://dev.tour.kr/image.png");

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

  @DisplayName("태그가 있는 여행기를 작성한다.")
  @Test
  void createTravelogueWithTags() {
    Mockito.when(s3Provider.copyImageToPermanentStorage(any(String.class)))
            .thenReturn(TravelogueResponseFixture.getTravelogueResponse().thumbnail());

    testHelper.initTagTestData(TagFixture.TAG_1.get());

    List<TravelogueDayRequest> days = getTravelogueDayRequests();
    TravelogueRequest request = TravelogueRequestFixture.getTravelogueRequest(days, List.of(1L));

    RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .body(request)
            .when().post("/api/v1/travelogues")
            .then().log().all()
            .statusCode(201)
            .header("Location", "/api/v1/travelogues/1");
  }

  @DisplayName("최대 업로드 가능 개수 이상의 사진을 포함한 여행기를 작성하면 예외가 발생한다.")
  @Test
  void createTravelogueWithOver10PhotosEachPlaces() throws JsonProcessingException {
    Mockito.when(s3Provider.copyImageToPermanentStorage(any(String.class)))
            .thenReturn(TravelogueResponseFixture.getTravelogueResponse().thumbnail());

    List<TraveloguePhotoRequest> photos = TravelogueRequestFixture.getTraveloguePhotoRequestsOverLimit();
    List<TraveloguePlaceRequest> places = TravelogueRequestFixture.getTraveloguePlaceRequests(photos);
    List<TravelogueDayRequest> days = TravelogueRequestFixture.getTravelogueDayRequests(places);
    TravelogueRequest request = TravelogueRequestFixture.getTravelogueRequest(days);
    Member member = testHelper.initKakaoMemberTestData();
    String accessToken = jwtTokenProvider.createToken(member.getId())
            .accessToken();

    ExceptionResponse response = new ExceptionResponse("여행기 장소 사진은 최대 10개입니다.");

    RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .body(request)
            .when().post("/api/v1/travelogues")
            .then().log().all()
            .statusCode(400).assertThat()
            .body(is(objectMapper.writeValueAsString(response)));
  }

  @DisplayName("최소 여행 일자 개수를 만족하지 않은 여행기를 작성하려하면 예외가 발생한다.")
  @Test
  void createTravelogueWithNoDays() throws JsonProcessingException {
    Mockito.when(s3Provider.copyImageToPermanentStorage(any(String.class)))
            .thenReturn(TravelogueResponseFixture.getTravelogueResponse().thumbnail());

    TravelogueRequest request = TravelogueRequestFixture.getTravelogueRequest(List.of());

    ExceptionResponse response = new ExceptionResponse("여행기 일자는 최소 1일은 포함되어야 합니다.");

    RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .body(request)
            .when().post("/api/v1/travelogues")
            .then().log().all()
            .statusCode(400).assertThat()
            .body(is(objectMapper.writeValueAsString(response)));
  }

  @DisplayName("여행기를 작성할 때 로그인 되어 있지 않으면 예외가 발생한다.")
  @Test
  void createTravelogueWithNotLoginThrowException() {
    List<TravelogueDayRequest> days = getTravelogueDayRequests();
    TravelogueRequest request = TravelogueRequestFixture.getTravelogueRequest(days);

    RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/api/v1/travelogues")
            .then().log().all()
            .statusCode(401)
            .body("message", is("로그인을 해주세요."));
  }


  @DisplayName("여행기를 상세 조회한다.")
  @Test
  void findTravelogue() throws JsonProcessingException {
    testHelper.initTravelogueTestData(member);
    TravelogueResponse response = TravelogueResponseFixture.getTravelogueResponse();

    RestAssured.given().log().all()
            .accept(ContentType.JSON)
            .when().get("/api/v1/travelogues/1")
            .then().log().all()
            .statusCode(200).assertThat()
            .body(is(objectMapper.writeValueAsString(response)));
  }

  @DisplayName("여행기에 좋아요를 한다.")
  @Test
  void likeTravelogue() throws JsonProcessingException {
    Member author = testHelper.initKakaoMemberTestData();
    testHelper.initTravelogueTestData(author);
    TravelogueLikeResponse response = new TravelogueLikeResponse(true, 1L);

    RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .when().post("/api/v1/travelogues/1/like")
            .then().log().all()
            .statusCode(200).assertThat()
            .body(is(objectMapper.writeValueAsString(response)));
  }

  @DisplayName("여행기를 좋아요 할 때 로그인 되어 있지 않으면 예외가 발생한다.")
  @Test
  void likeTravelogueWithNotLoginThrowException() {
    Member author = testHelper.initKakaoMemberTestData();
    testHelper.initTravelogueTestData(author);

    RestAssured.given().log().all()
            .when().post("/api/v1/travelogues/1/like")
            .then().log().all()
            .statusCode(401)
            .body("message", is("로그인을 해주세요."));
  }

  @DisplayName("태그가 있는 여행기를 상세 조회한다.")
  @Test
  void findTravelogueWithTags() throws JsonProcessingException {
    testHelper.initTravelogueTestDataWithTag(member);
    TravelogueResponse response = TravelogueResponseFixture.getTravelogueResponseWithTag();

    RestAssured.given().log().all()
            .accept(ContentType.JSON)
            .when().get("/api/v1/travelogues/1")
            .then().log().all()
            .statusCode(200).assertThat()
            .body(is(objectMapper.writeValueAsString(response)));
  }

  @DisplayName("메인 페이지 조회 시, 좋아요 순으로 여행기를 조회한다.")
  @Test
  void findMainPageTraveloguesOrderByLikeCount() throws JsonProcessingException {
    testHelper.initAllTravelogueTestData();
    Page<TravelogueSimpleResponse> responses = TravelogueResponseFixture.getTravelogueSimpleResponsesOrderByLikeCount();

    RestAssured.given().log().all()
            .accept(ContentType.JSON)
            .params("sort", "likeCount,asc")
            .when().get("/api/v1/travelogues")
            .then().log().all()
            .statusCode(200).assertThat()
            .body(is(objectMapper.writeValueAsString(responses)));
  }

}

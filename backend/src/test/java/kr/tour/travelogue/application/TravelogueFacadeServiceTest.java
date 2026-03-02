package kr.tour.travelogue.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import kr.tour.global.DatabaseCleaner;
import kr.tour.global.fixture.ServiceTest;
import kr.tour.global.config.TestJpaAuditingConfig;
import kr.tour.global.config.TestQueryDslConfig;
import kr.tour.global.dto.MemberAuth;
import kr.tour.image.infrastructure.AwsS3Provider;
import kr.tour.member.application.MemberService;
import kr.tour.member.domain.Member;
import kr.tour.travelogue.dto.request.*;
import kr.tour.travelogue.dto.response.TravelogueResponse;
import kr.tour.travelogue.dto.response.TravelogueSimpleResponse;
import kr.tour.travelogue.fixture.TravelogueRequestFixture;
import kr.tour.travelogue.fixture.TravelogueResponseFixture;
import kr.tour.travelogue.helper.TravelogueTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;


@DisplayName("여행기 Facade 시버스")
@Import(value = {
        TravelogueFacadeService.class,
        TravelogueService.class,
        AwsS3Provider.class,
        TravelogueImagePerpetuationService.class,
        TravelogueTagService.class,
        TravelogueCountryService.class,
        TravelogueLikeService.class,
        MemberService.class,
        TravelogueTestHelper.class,
        TestQueryDslConfig.class,
        TestJpaAuditingConfig.class
})
@ServiceTest
public class TravelogueFacadeServiceTest {

  private final TravelogueFacadeService service;

  private final TravelogueTestHelper testHelper;
  private final DatabaseCleaner databaseCleaner;
  @MockitoBean
  private final AwsS3Provider s3Provider;

  @Autowired
  public TravelogueFacadeServiceTest(
          TravelogueFacadeService travelogueFacadeService,
          TravelogueTestHelper travelogueTestHelper,
          DatabaseCleaner databaseCleaner,
          AwsS3Provider s3Provider
  ) {
    this.service = travelogueFacadeService;
    this.testHelper = travelogueTestHelper;
    this.databaseCleaner = databaseCleaner;
    this.s3Provider = s3Provider;
  }

  @BeforeEach
  void setUp(){
    databaseCleaner.executeTruncate();
  }
  private void mockImageCopyProcess() {
    given(s3Provider.copyImageToPermanentStorage(any(String.class)))
            .willReturn("https://dev.tour.kr/image.png");
  }
  @DisplayName("여행기를 생성할 수 있다")
  @Test
  void createTravelogue() {
    // given
    mockImageCopyProcess();
    testHelper.initKakaoMemberTestData();
    MemberAuth member = new MemberAuth(1L);
    List<TravelogueDayRequest> days = getTravelogueDayRequests();
    TravelogueRequest request = TravelogueRequestFixture.getTravelogueRequest(days);

    // when
    Long result = service.createTravelogue(member, request).id();

    // then
    assertThat(result).isEqualTo(1L);

  }

  @DisplayName("메인 페이지에 표시할 여행기 목록을 조회한다.")
  @Test
  void findSimpleTravelogues() {
    // given
    TravelogueSearchRequest searchRequest = new TravelogueSearchRequest(null, null);
    TravelogueFilterRequest filterRequest = new TravelogueFilterRequest(null, null);
    testHelper.initAllTravelogueTestData();
    Page<TravelogueSimpleResponse> expect = TravelogueResponseFixture.getTravelogueSimpleResponses();
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id"));

    // when
    Page<TravelogueSimpleResponse> result = service.findSimpleTravelogues(
            filterRequest,
            searchRequest,
            pageRequest
    );

    // then
    assertThat(result).containsAll(expect);
  }

  @DisplayName("필터링된 여행기 목록을 조회한다.")
  @Test
  void filterTravelogues() {
    // given
    testHelper.initAllTravelogueTestData();
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id"));
    TravelogueFilterRequest filter = new TravelogueFilterRequest(List.of(1L), null);
    TravelogueSearchRequest searchRequest = new TravelogueSearchRequest(null, null);

    // when
    Page<TravelogueSimpleResponse> result = service.findSimpleTravelogues(filter, searchRequest, pageRequest);

    // then
    assertThat(result.getContent()).hasSize(1);
  }

  @DisplayName("제목 키워드를 기반으로 여행기 목록을 조회한다.")
  @Test
  void findTraveloguesByTitleKeyword() {
    // given
    testHelper.initAllTravelogueTestData();
    Page<TravelogueSimpleResponse> responses = TravelogueResponseFixture.getTravelogueSimpleResponses();

    TravelogueSearchRequest searchRequest = new TravelogueSearchRequest("광안리", "title");
    TravelogueFilterRequest filterRequest = new TravelogueFilterRequest(null, null);
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id"));

    // when
    Page<TravelogueSimpleResponse> searchResults = service.findSimpleTravelogues(
            filterRequest,
            searchRequest,
            pageRequest
    );

    // then
    assertThat(searchResults).containsAll(responses);
  }

  @DisplayName("여행기를 ID와 로그인한 사용자를 기준으로 조회한다.")
  @Test
  void findTravelogueByIdForAuthenticated() {
    // given
    Member member = testHelper.initKakaoMemberTestData();
    Long travelogueId = testHelper.initTravelogueTestDataWithLike(member).getId();

    // when
    TravelogueResponse result = service.findTravelogueByIdForAuthenticated(travelogueId, new MemberAuth(member.getId()));

    // then
    assertThat(result)
            .isEqualTo(TravelogueResponseFixture.getTravelogueResponseWithLike());
  }

  private List<TravelogueDayRequest> getTravelogueDayRequests() {
    List<TraveloguePhotoRequest> photos = TravelogueRequestFixture.getTraveloguePhotoRequests();
    List<TraveloguePlaceRequest> places = TravelogueRequestFixture.getTraveloguePlaceRequests(photos);
    return TravelogueRequestFixture.getTravelogueDayRequests(places);
  }

  @DisplayName("사용자 닉네임을 기반으로 여행기 목록을 조회한다.")
  @Test
  void findTraveloguesByAuthorNicknameKeyword() {
    // given
    testHelper.initAllTravelogueTestData();
    Page<TravelogueSimpleResponse> responses = TravelogueResponseFixture.getTravelogueSimpleResponses();

    TravelogueSearchRequest searchRequest = new TravelogueSearchRequest("테스터", "author");
    TravelogueFilterRequest filterRequest = new TravelogueFilterRequest(null, null);
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id"));

    // when
    Page<TravelogueSimpleResponse> searchResults = service.findSimpleTravelogues(
            filterRequest,
            searchRequest,
            pageRequest
    );

    // then
    assertThat(searchResults).containsAll(responses);
  }

  @DisplayName("국가 코드를 기반으로 여행기 목록을 조회한다.")
  @Test
  void findTraveloguesByCountryCodeKeyword() {
    // given
    testHelper.initAllTravelogueTestData();
    Page<TravelogueSimpleResponse> responses = TravelogueResponseFixture.getTravelogueSimpleResponses();

    TravelogueSearchRequest searchRequest = new TravelogueSearchRequest("한국", "country");
    TravelogueFilterRequest filterRequest = new TravelogueFilterRequest(null, null);
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id"));

    // when
    Page<TravelogueSimpleResponse> searchResults = service.findSimpleTravelogues(filterRequest, searchRequest,
            pageRequest);

    // then
    assertThat(searchResults).containsAll(responses);
  }

  @DisplayName("여행기를 수정할 수 있다.")
  @Test
  void updateTravelogue() {
    given(s3Provider.copyImageToPermanentStorage(any(String.class)))
            .willReturn("https://dev.tour.kr/image.png");

    List<TravelogueDayRequest> days = getUpdateTravelogueDayRequests();

    Member author = testHelper.initKakaoMemberTestData();
    testHelper.initTravelogueTestData(author);

    MemberAuth memberAuth = new MemberAuth(author.getId());
    TravelogueRequest request = TravelogueRequestFixture.getUpdateTravelogueRequest(days);
    String updatedTitle = request.title();
    TravelogueResponse updatedResponse = service.updateTravelogue(1L, memberAuth, request);

    assertThat(updatedResponse.title()).isEqualTo(updatedTitle);
  }

  private List<TravelogueDayRequest> getUpdateTravelogueDayRequests() {
    List<TraveloguePhotoRequest> photos = TravelogueRequestFixture.getTraveloguePhotoRequests();
    List<TraveloguePlaceRequest> places = TravelogueRequestFixture.getUpdateTraveloguePlaceRequests(photos);
    return TravelogueRequestFixture.getUpdateTravelogueDayRequests(places);
  }
}

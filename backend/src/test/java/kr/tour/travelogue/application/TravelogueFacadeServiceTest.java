package kr.tour.travelogue.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kr.tour.global.DatabaseCleaner;
import kr.tour.global.fixture.ServiceTest;
import kr.tour.global.config.TestJpaAuditingConfig;
import kr.tour.global.config.TestQueryDslConfig;
import kr.tour.global.dto.MemberAuth;
import kr.tour.image.infrastructure.AwsS3Provider;
import kr.tour.member.application.MemberService;
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
import org.springframework.context.annotation.Import;
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
    when(s3Provider.copyImageToPermanentStorage(any(String.class)))
            .thenReturn("https://dev.touroot.kr/image.png");
  }
  @DisplayName("여행기를 생성할 수 있다")
  @Test
  void createTravelogue() {
    mockImageCopyProcess();
    testHelper.initKakaoMemberTestData();
    MemberAuth member = new MemberAuth(1L);
    List<TravelogueDayRequest> days = getTravelogueDayRequests();
    TravelogueRequest request = TravelogueRequestFixture.getTravelogueRequest(days);

    assertThat(service.createTravelogue(member, request).id()).isEqualTo(1L);

  }

  private List<TravelogueDayRequest> getTravelogueDayRequests() {
    List<TraveloguePhotoRequest> photos = TravelogueRequestFixture.getTraveloguePhotoRequests();
    List<TraveloguePlaceRequest> places = TravelogueRequestFixture.getTraveloguePlaceRequests(photos);
    return TravelogueRequestFixture.getTravelogueDayRequests(places);
  }
}

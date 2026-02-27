package kr.tour.travelogue.presentation;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import kr.tour.global.fixture.ControllerTest;
import kr.tour.travelogue.application.TravelogueFacadeService;
import kr.tour.travelogue.dto.request.TravelogueDayRequest;
import kr.tour.travelogue.dto.request.TraveloguePhotoRequest;
import kr.tour.travelogue.dto.request.TraveloguePlaceRequest;
import kr.tour.travelogue.dto.request.TravelogueRequest;
import kr.tour.travelogue.dto.response.TravelogueCreateResponse;
import kr.tour.travelogue.fixture.TravelogueRequestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TravelogueController.class)
@DisplayName("여행기 컨트롤러")
class TravelogueControllerTest extends ControllerTest {

  @MockitoBean
  private TravelogueFacadeService travelogueFacadeService;

  @DisplayName("여행기를 작성한다.")
  @Test
  void createTravelogueWithoutTagV1_OK() throws Exception {
    Long travelogueId = 1L;
    List<TravelogueDayRequest> days = getTravelogueDayRequests();
    TravelogueRequest request = TravelogueRequestFixture.getTravelogueRequest(days);
    var response = TravelogueCreateResponse.builder().id(travelogueId).build();

    given(travelogueFacadeService.createTravelogue(any(),any())).willReturn(response);
    given(jwtTokenProvider.decodeAccessToken(any())).willReturn("1");
    mockMvc.perform(post("/api/v1/travelogues")
                    .header("Authorization", "Bearer valid-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(document("v1-create-travelogue-ok",
                    resource(ResourceSnippetParameters.builder()
                            .tag("Create Travelogue API")
                            .summary("여행기 등록 V1")
                            .description("여행기 정보를 등록합니다.")
                            .requestFields(getTravelogueRequestFields())
                            .build())
                    ))
            .andExpect(header().string("Location","/api/v1/travelogues/"+travelogueId));
  }

  private List<TravelogueDayRequest> getTravelogueDayRequests() {
    List<TraveloguePhotoRequest> photos = TravelogueRequestFixture.getTraveloguePhotoRequests();
    List<TraveloguePlaceRequest> places = TravelogueRequestFixture.getTraveloguePlaceRequests(photos);
    return TravelogueRequestFixture.getTravelogueDayRequests(places);
  }

  private FieldDescriptor[] getTravelogueRequestFields() {
    return new FieldDescriptor[]{
            fieldWithPath("title").type(JsonFieldType.STRING).description("여행기 제목"),
            fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("썸네일 이미지 URL"),
            fieldWithPath("tags").type(JsonFieldType.ARRAY).description("태그 목록 (빈 배열 가능)"),

            // days 배열 관련
            fieldWithPath("days").type(JsonFieldType.ARRAY).description("날짜별 일정 목록"),

            // days[].places 배열 관련
            fieldWithPath("days[].places").type(JsonFieldType.ARRAY).description("해당 날짜의 장소 목록"),
            fieldWithPath("days[].places[].placeName").type(JsonFieldType.STRING).description("장소 이름"),

            // days[].places[].position 객체 관련
            fieldWithPath("days[].places[].position").type(JsonFieldType.OBJECT).description("장소 위치 정보"),
            fieldWithPath("days[].places[].position.lat").type(JsonFieldType.STRING).description("위도"),
            fieldWithPath("days[].places[].position.lng").type(JsonFieldType.STRING).description("경도"),

            fieldWithPath("days[].places[].description").type(JsonFieldType.STRING).description("장소 설명"),
            fieldWithPath("days[].places[].countryCode").type(JsonFieldType.STRING).description("국가 코드 (예: KR)"),

            // days[].places[].photoUrls 배열 관련
            fieldWithPath("days[].places[].photoUrls").type(JsonFieldType.ARRAY).description("장소 사진 목록"),
            fieldWithPath("days[].places[].photoUrls[].url").type(JsonFieldType.STRING).description("사진 URL")
    };
  }
}
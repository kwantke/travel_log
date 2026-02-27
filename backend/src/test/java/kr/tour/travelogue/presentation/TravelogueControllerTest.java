package kr.tour.travelogue.presentation;

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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
            .andExpect(header().string("Location","/api/v1/travelogues/"+travelogueId));
  }

  private List<TravelogueDayRequest> getTravelogueDayRequests() {
    List<TraveloguePhotoRequest> photos = TravelogueRequestFixture.getTraveloguePhotoRequests();
    List<TraveloguePlaceRequest> places = TravelogueRequestFixture.getTraveloguePlaceRequests(photos);
    return TravelogueRequestFixture.getTravelogueDayRequests(places);
  }
}
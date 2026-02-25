package kr.tour.travelogue.fixture;

import kr.tour.travelogue.dto.request.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TravelogueRequestFixture {

  public static List<TraveloguePhotoRequest> getTraveloguePhotoRequests() {
    return List.of(new TraveloguePhotoRequest("https://dev.tour.kr/temporary/image1.png"));
  }

  public static List<TraveloguePlaceRequest> getTraveloguePlaceRequests(List<TraveloguePhotoRequest> photos) {
    return List.of(new TraveloguePlaceRequest(
            "대천 해수욕장", getTraveloguePositionRequest(),
            "에메랄드 빛 해변은 해외 휴양지와 견줘도 밀리지 않습니다.",
            photos,
            "KR"
    ),
            new TraveloguePlaceRequest(
                    "광안리 해수욕장", getTraveloguePositionRequest(),
                    "광안리 해변은 해외 휴양지와 견줘도 밀리지 않습니다.",
                    photos,
                    "KR"
            ));
  }

  public static TraveloguePositionRequest getTraveloguePositionRequest() {
    return new TraveloguePositionRequest("34.54343", "126.66977");
  }

  public static List<TravelogueDayRequest> getTravelogueDayRequests(List<TraveloguePlaceRequest> places) {
    return List.of(new TravelogueDayRequest(places));
  }

  public static TravelogueRequest getTravelogueRequest(List<TravelogueDayRequest> days) {
    return new TravelogueRequest(
            "부산 여행지",
            "https://dev.tour.kr/temporary/busan_thumbnail.png",
            List.of(),
            days
    );
  }
}

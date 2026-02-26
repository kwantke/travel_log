package kr.tour.travelogue.fixture;

import kr.tour.travelogue.domain.TraveloguePhoto;
import kr.tour.travelogue.domain.TraveloguePlace;
import lombok.AllArgsConstructor;

import static kr.tour.travelogue.fixture.TraveloguePlaceFixture.TRAVELOGUE_PLACE;


@AllArgsConstructor
public enum TraveloguePhotoFixture {

    TRAVELOGUE_PHOTO(1, "https://dev.tour.kr/temporary/image1.png", TRAVELOGUE_PLACE.get());

    private final int order;
    private final String url;
    private final TraveloguePlace place;

    public TraveloguePhoto get() {
        return new TraveloguePhoto(order, url, place);
    }

    public TraveloguePhoto create(TraveloguePlace place) {
        return new TraveloguePhoto(order, url, place);
    }
}

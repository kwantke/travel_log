package kr.tour.travelogue.fixture;


import kr.tour.travelogue.domain.Tag;
import kr.tour.travelogue.dto.response.TagResponse;

public enum TagFixture {

  TAG_1("강아지와 함께"),
  TAG_2("고양이와 함께"),
  TAG_3("알파카와 함께"),
  ;

  private final String tag;

  TagFixture(String tag) {
    this.tag = tag;
  }

  public Tag get() {
    return new Tag(tag);
  }

  public TagResponse getResponse(Long id) {
    return new TagResponse(id, tag);
  }
}

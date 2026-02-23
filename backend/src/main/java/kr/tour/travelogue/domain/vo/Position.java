package kr.tour.travelogue.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Position {

  @Column(nullable = false)
  private String latitude;
  @Column(nullable = false)
  private String longitude;

  public Position(String latitude, String longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
}

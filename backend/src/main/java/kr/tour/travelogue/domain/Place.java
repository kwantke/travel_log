package kr.tour.travelogue.domain;

import jakarta.persistence.*;
import kr.tour.global.entity.AuditingFields;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = @Index(name="idx_place_name_latitude_longitude", columnList = "name, latitude, longitude"))
public class Place extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String latitude;

  @Column(nullable = false)
  private String longitude;

  private String googlePlaceId;

  public Place(Long id, String name, String latitude, String longitude, String googlePlaceId) {
    this.id = id;
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.googlePlaceId = googlePlaceId;
  }
}

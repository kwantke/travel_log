package kr.tour.travelogue.domain;

import jakarta.persistence.*;
import kr.tour.global.entity.AuditingFields;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE travelogue_photo SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Entity
public class TraveloguePhoto extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="PHOTO_KEY", nullable = false)
  private String key;

  @Column(name = "PHOTO_ORDER", nullable = false)
  private Integer order;

  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private TraveloguePlace traveloguePlace;

  public TraveloguePhoto(Long id, Integer order, String key, TraveloguePlace traveloguePlace) {
    this.id = id;
    this.key = key;
    this.order = order;
    this.traveloguePlace = traveloguePlace;
  }

  public TraveloguePhoto(Integer order, String key, TraveloguePlace traveloguePlace) {
    this(null, order, key, traveloguePlace);
  }


  public void updateTraveloguePlace(TraveloguePlace traveloguePlace) {
    this.traveloguePlace = traveloguePlace;
  }

  public void updateKey(String key) {
    this.key = key;
  }
}

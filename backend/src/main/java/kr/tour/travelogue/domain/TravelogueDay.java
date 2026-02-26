package kr.tour.travelogue.domain;

import jakarta.persistence.*;
import kr.tour.global.entity.AuditingFields;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE travelogue_day SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Entity
public class TravelogueDay extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "DAY_ORDER", nullable = false)
  private Integer order;

  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Travelogue travelogue;

  @OneToMany(mappedBy = "travelogueDay", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TraveloguePlace> traveloguePlaces = new ArrayList<>();

  public TravelogueDay(Object o, Integer order, Travelogue travelogue) {
    this.id = id;
    this.order = order;
    this.travelogue = travelogue;
  }

  public TravelogueDay(Integer order, Travelogue travelogue) {
    this(null, order, travelogue);
  }


  public void addPlace(TraveloguePlace traveloguePlace) {
    traveloguePlaces.add(traveloguePlace);
    traveloguePlace.updateTravelogueDay(this);
  }

  public void updateTravelogue(Travelogue travelogue) {
    this.travelogue = travelogue;
  }
}

package kr.tour.travelogue.domain;

import jakarta.persistence.*;
import kr.tour.global.entity.AuditingFields;
import kr.tour.travelogue.domain.enums.CountryCode;
import kr.tour.travelogue.domain.vo.Position;
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
@SQLDelete(sql = "UPDATE travelogue_place SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Entity
public class TraveloguePlace extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="PLACE_ORDER", nullable = false)
  private Integer order;

  private String description;
  @Column(nullable = false)
  private String name;

  @Embedded
  private Position position;

  @ManyToOne(fetch = FetchType.LAZY)
  private Place place;

  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private TravelogueDay travelogueDay;

  @OneToMany(mappedBy = "traveloguePlace", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TraveloguePhoto> traveloguePhotos = new ArrayList<>();

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CountryCode countryCode;

  public TraveloguePlace(
          Long id,
          Integer order,
          String description,
          String name,
          Position position,
          TravelogueDay travelogueDay,
          String countryCode
  ) {

    this.id = id;
    this.order = order;
    this.description = description;
    this.name = name;
    this.position = position;
    this.travelogueDay = travelogueDay;
    this.countryCode = CountryCode.from(countryCode);
  }

}

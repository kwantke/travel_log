package kr.tour.travelogue.domain;

import jakarta.persistence.*;
import kr.tour.travelogue.domain.enums.CountryCode;
import lombok.*;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class TravelogueCountry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Travelogue travelogue;

  @Enumerated(EnumType.STRING)
  private CountryCode countryCode;

  @Column(nullable = false)
  private Integer count;

  public TravelogueCountry(Travelogue travelogue, CountryCode countryCode, Integer count) {
    this.travelogue = travelogue;
    this.countryCode = countryCode;
    this.count = count;
  }
}

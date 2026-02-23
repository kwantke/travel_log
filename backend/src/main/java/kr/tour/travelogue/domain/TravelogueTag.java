package kr.tour.travelogue.domain;


import jakarta.persistence.*;
import kr.tour.global.entity.AuditingFields;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ETag;

@Getter
@Table(
        name = "travelogue_tag",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_travelogue_tag",
                columnNames = {"TRAVELOGUE_ID", "TAG_ID"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class TravelogueTag extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name="TRAVELOGUE_ID", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Travelogue travelogue;

  @JoinColumn(name = "TAG_ID", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Tag tag;

  public TravelogueTag(Travelogue travelogue, Tag tag) {
    this(null, travelogue, tag);
  }

}

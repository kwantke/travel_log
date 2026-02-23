package kr.tour.travelogue.domain;

import jakarta.persistence.*;
import kr.tour.global.entity.AuditingFields;
import kr.tour.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql="UPDATE travelogue SET deleted_at = NOW() WHERE id = ?")
@Entity
public class Travelogue extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn
  @ManyToOne(fetch = FetchType.LAZY)
  private Member author;

  @Column(nullable = false, length = 20)
  private String title;
  @Column(nullable = false)
  private String thumbnail;
  @Column(nullable = false)
  private Long likeCount;

  @OneToMany(mappedBy = "travelogue", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TravelogueDay> travelogueDays = new ArrayList<>();

  @OneToMany(mappedBy = "travelogue")
  private List<TravelogueTag> travelogueTags = new ArrayList<>();

  private Travelogue(Long id, Member author, String title, String thumbnail, Long likeCount) {
    this.id = id;
    this.author = author;
    this.title = title;
    this.thumbnail = thumbnail;
    this.likeCount = likeCount;
  }

  public Travelogue(Member author, String title, String thumbnail) {
    this(null, author, title, thumbnail, 0L);
  }


}

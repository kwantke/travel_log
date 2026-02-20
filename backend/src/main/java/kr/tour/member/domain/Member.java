package kr.tour.member.domain;

import jakarta.persistence.*;
import kr.tour.global.entity.AuditingFields;
import kr.tour.member.domain.enums.LoginType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of="id", callSuper = false)
@Entity
public class Member extends AuditingFields {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long kakaoId;
  private String email;
  private String password;
  @Column(nullable = false)
  private String nickname;
  @Column(nullable = false)
  private String profileImageUrl;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private LoginType loginType;
}

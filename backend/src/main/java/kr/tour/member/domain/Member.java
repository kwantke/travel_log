package kr.tour.member.domain;

import jakarta.persistence.*;
import kr.tour.global.entity.AuditingFields;
import kr.tour.member.domain.enums.LoginType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

  public Member(
          Long id, Long kakaoId, String email, String password, String nickname, String url, LoginType loginType
  ) {

    this.id = id;
    this.kakaoId = kakaoId;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.profileImageUrl = url;
    this.loginType = loginType;
  }

  public Member(Long kakaoId, String nickname, String profileImageUrl, LoginType loginType) {
    this(null, kakaoId, null, null, nickname, profileImageUrl, loginType);
  }

  public Member(String email, String password, String nickname, String profileImageUrl, LoginType loginType) {
    this(null, null, email, password, nickname, profileImageUrl, loginType);
  }
}

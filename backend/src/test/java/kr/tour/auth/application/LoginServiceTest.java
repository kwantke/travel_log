package kr.tour.auth.application;

import kr.tour.auth.dto.response.LoginResponse;
import kr.tour.auth.dto.response.OauthUserInformationResponse;
import kr.tour.auth.dto.response.TokenResponse;
import kr.tour.auth.fixture.OauthUserInformationFixture;
import kr.tour.auth.infrastructure.KakaoOauthProvider;
import kr.tour.global.config.TestJpaAuditingConfig;
import kr.tour.global.fixture.ServiceTest;
import kr.tour.global.config.JwtTokenProvider;
import kr.tour.global.log.logger.JsonLogger;
import kr.tour.member.domain.Member;
import kr.tour.member.fixture.MemberFixture;
import kr.tour.member.infrastructure.MemberRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import(value = {
        LoginService.class,
        TestJpaAuditingConfig.class
})
@ServiceTest
class LoginServiceTest {
  private static final String AUTHENTICATION_CODE = "test-authentication-code";
  private static final String REDIRECT_URI = "http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fv1%2Flogin%2Foauth%2Fkakao";
  private static final Member MEMBER = MemberFixture.KAKAO_MEMBER.build();

  @Autowired // Mock이 아니라 실제 빈을 주입받음
  private LoginService loginService;
  @MockitoBean // 외부 통신은 가짜로 처리
  private KakaoOauthProvider kakaoOauthProvider;

  @Autowired // DB 접근도 가짜로 처리 (단위 테스트라면)
  private MemberRepository memberRepository;

  @MockitoBean // 토큰 생성도 가짜로 처리
  private JwtTokenProvider jwtTokenProvider;

  @MockitoBean // 로거도 가짜로 처리
  private JsonLogger jsonLogger;

  @MockitoBean
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("회원가입이 되어 있지 않은 회원은 소셜 로그인 과정에서 회원가입 후 로그인 된다")
  void notExistUserKakaoSocialLoginTest() {

    //given
    String accessToken = "access12341234.token12341234.fake-token";
    String refreshToken = "refresh12341234.token12341234.fake-token";

    // 카카오에서 정보를 가져오는 것까지만 Mocking
    OauthUserInformationResponse userInfo = OauthUserInformationFixture.OAUTH_USER_1_INFORMATION;
    given(kakaoOauthProvider.getUserInformation(any(), any())).willReturn(userInfo);

    // JwtTokenProvider도 Mock이므로 반환값 설정 (ID는 DB에서 생성되므로 any() 권장)
    given(jwtTokenProvider.createToken(any())).willReturn(new TokenResponse(accessToken, refreshToken));

    // when
    LoginResponse response = loginService.oauthLogin(AUTHENTICATION_CODE, REDIRECT_URI);

    // then
    SoftAssertions.assertSoftly(softly->{
      softly.assertThat(response.accessToken()).isNotNull();
      softly.assertThat(response.refreshToken()).isNotNull();
      Member savedMember = memberRepository.findByKakaoId(userInfo.socialLoginId()).orElseThrow();
      softly.assertThat(response.nickname()).isEqualTo(savedMember.getNickname());
    });

  }
}
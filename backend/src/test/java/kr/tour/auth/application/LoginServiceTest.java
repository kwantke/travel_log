package kr.tour.auth.application;

import kr.tour.auth.dto.response.LoginResponse;
import kr.tour.auth.dto.response.TokenResponse;
import kr.tour.auth.fixture.OauthUserInformationFixture;
import kr.tour.auth.infrastructure.KakaoOauthProvider;
import kr.tour.global.fixture.ServiceTest;
import kr.tour.global.config.JwtTokenProvider;
import kr.tour.member.domain.Member;
import kr.tour.member.fixture.MemberFixture;
import kr.tour.member.infrastructure.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ServiceTest
class LoginServiceTest {
  private static final String AUTHENTICATION_CODE = "test-authentication-code";
  private static final String REDIRECT_URI = "http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fv1%2Flogin%2Foauth%2Fkakao";
  private static final Member MEMBER = MemberFixture.KAKAO_MEMBER.build();

  @InjectMocks
  private LoginService loginService;

  @Mock
  private KakaoOauthProvider kakaoOauthProvider;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private JwtTokenProvider jwtTokenProvider;


  @Test
  @DisplayName("회원가입이 되어 있지 않은 회원은 소셜 로그인 과정에서 회원가입 후 로그인 된다")
  void notExistUserKakaoSocialLoginTest() {

    //Given
    String accessToken = "access12341234.token12341234.fake-token";
    String refreshToken = "refresh12341234.token12341234.fake-token";

    given(kakaoOauthProvider.getUserInformation(any(String.class), any(String.class)))
            .willReturn(OauthUserInformationFixture.OAUTH_USER_1_INFORMATION);
    given(memberRepository.findByKakaoId(any())).willReturn(Optional.empty());
    given(memberRepository.save(any(Member.class))).willReturn(MEMBER);
    given(jwtTokenProvider.createToken(MEMBER.getId()))
            .willReturn(new TokenResponse(accessToken, refreshToken));

    LoginResponse response = loginService.oauthLogin(AUTHENTICATION_CODE, REDIRECT_URI);

    //When & Then
    assertThat(response).isEqualTo(
            LoginResponse.of(MEMBER, new TokenResponse(response.accessToken(), response.refreshToken()))
    );
  }
}
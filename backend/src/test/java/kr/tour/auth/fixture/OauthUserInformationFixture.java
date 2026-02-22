package kr.tour.auth.fixture;

import kr.tour.auth.dto.response.OauthUserInformationResponse;
import kr.tour.auth.dto.response.kakao.KakaoAccount;
import kr.tour.auth.dto.response.kakao.KakaoProfile;

public class OauthUserInformationFixture {

  public static final OauthUserInformationResponse OAUTH_USER_1_INFORMATION = new OauthUserInformationResponse(
          1L, new KakaoAccount(new KakaoProfile("리비", "http://img-url.com"))
  );
}

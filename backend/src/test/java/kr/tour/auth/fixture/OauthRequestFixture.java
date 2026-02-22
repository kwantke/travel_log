package kr.tour.auth.fixture;

import kr.tour.auth.dto.request.OauthLoginRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OauthRequestFixture {
  OAUTH_REQUEST_FIXTURE(
          new OauthLoginRequest("test", "https://test")
  ),;

  private final OauthLoginRequest request;
}

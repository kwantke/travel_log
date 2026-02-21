package kr.tour.auth.infrastructure;

import kr.tour.auth.dto.response.OauthUserInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KakaoOauthProvider {
  private final KakaoOauthClient kakaoOauthClient;

  public OauthUserInformationResponse getUserInformation(String authorizationCode, String redirectUri) {
    return kakaoOauthClient.requestUserInformation(authorizationCode, redirectUri);
  }
}


package kr.tour.auth.infrastructure;


import java.io.IOException;

import kr.tour.auth.dto.response.OauthUserInformationResponse;
import kr.tour.auth.dto.response.kakao.KakaoAccessTokenResponse;
import kr.tour.global.exception.CoreException;
import kr.tour.member.domain.exception.MemberErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class KakaoOauthClient {

  private final String userInformationRequestUri;
  private final String accessTokenRequestUri;
  private final String restApiKey;
  private final String clientSecret;

  private final RestClient kakaoRestClient;

  public KakaoOauthClient(
          @Value("${oauth.kakao.user-information-request-uri}") String userInformationRequestUri,
          @Value("${oauth.kakao.access-token-request-uri}") String accessTokenRequestUri,
          @Value("${oauth.kakao.rest-api-key}") String restApiKey,
          @Value("${oauth.kakao.client-secret}")String clientSecret,
          @Qualifier("restClient")
          RestClient kakaoRestClient
  ) {
    this.userInformationRequestUri = userInformationRequestUri;
    this.accessTokenRequestUri = accessTokenRequestUri;
    this.restApiKey = restApiKey;
    this.clientSecret = clientSecret;
    this.kakaoRestClient = kakaoRestClient;
  }



  public OauthUserInformationResponse requestUserInformation(String authorizationCode, String redirectUri) {
    KakaoAccessTokenResponse kakaoAccessTokenResponse = requestAccessToken(authorizationCode, redirectUri);

    return kakaoRestClient.get()
            .uri(userInformationRequestUri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessTokenResponse.accessToken())
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleClientError)
            .onStatus(HttpStatusCode::is2xxSuccessful, this::handleSuccessLogging)
            .toEntity(OauthUserInformationResponse.class)
            .getBody();
  }

  private KakaoAccessTokenResponse requestAccessToken(String authorizationCode, String redirectUri) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", authorizationCode);
    params.add("client_id", restApiKey);
    params.add("client_secret", clientSecret);
    params.add("redirect_uri", redirectUri);
    params.add("grant_type", "authorization_code");

    return kakaoRestClient.post()
            .uri(accessTokenRequestUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(params)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleClientError)
            .onStatus(HttpStatusCode::is2xxSuccessful, this::handleSuccessLogging)
            .toEntity(KakaoAccessTokenResponse.class)
            .getBody();
  }

  private void handleClientError(HttpRequest request, ClientHttpResponse response) throws IOException {
    log.error("KakaoOauth:: {} {} ({})", request.getMethod(), request.getURI(), response.getStatusCode());

    if (response.getStatusCode().is4xxClientError()) {
      throw new CoreException(MemberErrorCode.INVALID_KAKAO_LOGIN_INFO);
    }
    throw new CoreException(MemberErrorCode.INTERNAL_ERROR_KAKAO_SERVER);
  }

  private void handleSuccessLogging(HttpRequest request, ClientHttpResponse response) throws IOException {
    log.info("KakaoOauth:: {} {} ({})", request.getMethod(), request.getURI(), response.getStatusCode());
  }
}


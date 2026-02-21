package kr.tour.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.tour.auth.dto.response.kakao.KakaoAccount;
import kr.tour.member.domain.Member;
import kr.tour.member.domain.enums.LoginType;
import lombok.Builder;

@Builder
public record OauthUserInformationResponse (
        @JsonProperty("id")
        Long socialLoginId,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
){
  public Member toMember() {
    return new Member(socialLoginId, nickname(), profileImage(), LoginType.KAKAO);
  }
  public String nickname(){
    return kakaoAccount.kakaoProfile().nickname();
  }

  public String profileImage() {
    return kakaoAccount.kakaoProfile().image();
  }
}

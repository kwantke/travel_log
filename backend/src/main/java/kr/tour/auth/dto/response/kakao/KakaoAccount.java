package kr.tour.auth.dto.response.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.tour.auth.dto.response.kakao.KakaoProfile;

public record KakaoAccount(
        @JsonProperty("profile")
        KakaoProfile kakaoProfile
) {
}

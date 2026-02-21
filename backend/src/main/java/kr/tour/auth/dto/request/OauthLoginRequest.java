package kr.tour.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OauthLoginRequest(
        @NotBlank(message = "카카오 권한 코드는 필수 값입니다.")
        String code,
        @NotBlank(message = "리다이엑트 URL은 필수 값입니다.")
        String redirectUri
) {

}

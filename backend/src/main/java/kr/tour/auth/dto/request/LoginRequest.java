package kr.tour.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.tour.global.log.mask.Masking;
import kr.tour.global.log.mask.MaskingType;

public record LoginRequest(
        @NotBlank(message = "이메일은 비어있을 수 없습니다.")
        @Email
        String email,
        @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
        @Masking(type = MaskingType.FULL)
        String password
) {

}

package kr.tour.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.tour.member.domain.Member;
import lombok.Builder;

@Builder
public record LoginResponse(
        Long memberId,
        String nickname,
        String profileImageUrl,
        String accessToken,
        String refreshToken
){
  public static LoginResponse of(Member m, TokenResponse t) {
    return LoginResponse.builder()
            .memberId(m.getId())
            .nickname(m.getNickname())
            .profileImageUrl(m.getProfileImageUrl())
            .accessToken(t.accessToken())
            .refreshToken(t.refreshToken())
            .build();
  }
}

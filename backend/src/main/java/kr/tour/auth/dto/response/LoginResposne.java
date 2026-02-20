package kr.tour.auth.dto.response;

import kr.tour.member.domain.Member;
import lombok.Builder;

@Builder
public record LoginResposne (
        Long memberId,
        String nickname,
        String profileImageUrl,
        String accessToken,
        String refreshToken
){
  public static LoginResposne of(Member m, TokenResponse t) {
    return LoginResposne.builder()
            .memberId(m.getId())
            .nickname(m.getNickname())
            .profileImageUrl(m.getProfileImageUrl())
            .accessToken(t.accessToken())
            .refreshToken(t.refreshToken())
            .build();
  }
}

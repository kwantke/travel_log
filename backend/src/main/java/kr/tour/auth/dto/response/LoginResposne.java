package kr.tour.auth.dto.response;

public record LoginResposne (
        Long memberId,
        String nickname,
        String profileImageUrl,
        String accessToken,
        String refreshToken
){

}

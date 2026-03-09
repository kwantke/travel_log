package kr.tour.auth.application;

import kr.tour.auth.dto.request.LoginRequest;
import kr.tour.auth.dto.request.TokenReissueRequest;
import kr.tour.auth.dto.response.LoginResponse;
import kr.tour.auth.dto.response.OauthUserInformationResponse;
import kr.tour.auth.infrastructure.KakaoOauthProvider;
import kr.tour.global.config.JwtTokenProvider;
import kr.tour.global.exception.CoreException;
import kr.tour.global.log.logger.JsonLogger;
import kr.tour.global.log.property.auth.LoginLogProperty;
import kr.tour.global.log.property.auth.SignUpLogProperty;
import kr.tour.member.domain.Member;
import kr.tour.member.domain.enums.LoginType;
import kr.tour.member.domain.exception.MemberErrorCode;
import kr.tour.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
public class LoginService {

  private final JsonLogger jsonLogger;

  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final KakaoOauthProvider kakaoOauthProvider;
  private final PasswordEncoder passwordEncoder;


  @Transactional(readOnly = true)
  public LoginResponse login(LoginRequest request) {
    Member member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new CoreException(MemberErrorCode.INVALID_MEMBER_INFO));

    validatePassword(request, member);

    jsonLogger.info(LoginLogProperty.successLocal(member));
    return LoginResponse.of(member, jwtTokenProvider.createToken(member.getId()));
  }

  private void validatePassword(LoginRequest request, Member member) {
    if (!passwordEncoder.matches(request.password(), member.getPassword())) {
      throw new CoreException(MemberErrorCode.INVALID_MEMBER_INFO);
    }
  }

  public LoginResponse oauthLogin(String code, String encodedRedirectUri) {
    String redirectUri = URLDecoder.decode(encodedRedirectUri, StandardCharsets.UTF_8);
    OauthUserInformationResponse userInfo = kakaoOauthProvider.getUserInformation(code, redirectUri);
    Member member = memberRepository.findByKakaoId(userInfo.socialLoginId())
            .orElseGet(() -> signUp(userInfo));

    jsonLogger.info(LoginLogProperty.successOAuth(member, LoginType.KAKAO));
    return LoginResponse.of(member, jwtTokenProvider.createToken(member.getId()));
  }

  @Transactional
  public Member signUp(OauthUserInformationResponse userInfo) {
    Member savedMember = memberRepository.save(userInfo.toMember());

    jsonLogger.info(SignUpLogProperty.ofOAuth(savedMember, LoginType.KAKAO));
    return savedMember;
  }

  public LoginResponse reissueToken(TokenReissueRequest request) {
    String memberId = jwtTokenProvider.decodeRefreshToken(request.refreshToken());

    Member member = memberRepository.findById(Long.valueOf(memberId))
            .orElseThrow(() -> new CoreException(MemberErrorCode.NOT_FOUND_MEMBER));

    return LoginResponse.of(member, jwtTokenProvider.createToken(member.getId()));
  }
}

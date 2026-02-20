package kr.tour.auth.application;

import kr.tour.auth.dto.request.LoginRequest;
import kr.tour.auth.dto.response.LoginResposne;
import kr.tour.auth.infrastructure.JwtTokenProvider;
import kr.tour.global.exception.CoreException;
import kr.tour.global.exception.ErrorCode;
import kr.tour.member.domain.Member;
import kr.tour.member.domain.exception.MemberErrorCode;
import kr.tour.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;


  @Transactional(readOnly = true)
  public LoginResposne login(LoginRequest request) {
    Member member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new CoreException(MemberErrorCode.INVALID_MEMBER_INFO));

    validatePassword(request, member);
    return LoginResposne.of(member, jwtTokenProvider.createToken(member.getId()));
  }

  private void validatePassword(LoginRequest request, Member member) {
    if (!passwordEncoder.matches(request.password(), member.getPassword())) {
      throw new CoreException(MemberErrorCode.INVALID_MEMBER_INFO);
    }
  }

}

package kr.tour.member.application;

import kr.tour.global.exception.CoreException;
import kr.tour.member.domain.Member;
import kr.tour.member.domain.exception.MemberErrorCode;
import kr.tour.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  public Member getMemberById(Long memberId) {
    return memberRepository.findById(memberId)
            .orElseThrow(() -> new CoreException(MemberErrorCode.NOT_FOUND_MEMBER));
  }

}

package kr.tour.global.dto;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Hidden
@Builder
public record MemberAuth(@NotNull Long memberId) {

  public static MemberAuth from(Long memberId) {
    return MemberAuth.builder().memberId(memberId).build();
  }
}

package kr.tour.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.tour.auth.application.LoginService;
import kr.tour.auth.dto.request.LoginRequest;
import kr.tour.auth.dto.response.LoginResponse;
import kr.tour.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
public class AuthController {

  private final LoginService loginService;
  @Operation(summary = "여행기 서비스 자체 로그인")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "요청이 정상적으로 처리되었을 때"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "요청 Body에 올바르지 않은 이메일 또는 비밀번호가 전달되었을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          )
  })
  @PostMapping
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

    return ResponseEntity.ok().body(loginService.login(request));
  }

  @Operation(summary = "카카오 소셜 로그인")
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "요청이 정상적으로 처리되었을 때"
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "요청 파라미터에 올바르지 않은 인가코드값이 전달되었을 때",
                  content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
          )
  })
  @PostMapping("/oauth/kakao")
  public ResponseEntity<LoginResponse> kakaoLogin(
          @RequestParam(name = "code") String authorizationCode,
          @RequestParam(name = "redirectUri") String encodedRedirectUri
  ) {
    return ResponseEntity.ok()
            .body(loginService.oauthLogin(authorizationCode, encodedRedirectUri));
  }

}

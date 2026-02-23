package kr.tour.auth.presentation;

import jakarta.validation.Valid;
import kr.tour.auth.application.LoginService;
import kr.tour.auth.dto.request.LoginRequest;
import kr.tour.auth.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
public class AuthController {

  private final LoginService loginService;
  @PostMapping
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

    return ResponseEntity.ok().body(loginService.login(request));
  }

  @PostMapping("/oauth/kakao")
  public ResponseEntity<LoginResponse> kakaoLogin(
          @RequestParam(name = "code") String authorizationCode,
          @RequestParam(name = "redirectUri") String encodedRedirectUri
  ) {
    return ResponseEntity.ok()
            .body(loginService.oauthLogin(authorizationCode, encodedRedirectUri));
  }

}

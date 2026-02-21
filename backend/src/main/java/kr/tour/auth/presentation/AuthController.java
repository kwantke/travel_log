package kr.tour.auth.presentation;

import jakarta.validation.Valid;
import kr.tour.auth.application.LoginService;
import kr.tour.auth.dto.request.LoginRequest;
import kr.tour.auth.dto.request.OauthLoginRequest;
import kr.tour.auth.dto.response.LoginResposne;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
public class AuthController {

  private final LoginService loginService;
  @PostMapping
  public ResponseEntity<LoginResposne> login(@Valid @RequestBody LoginRequest request) {

    return ResponseEntity.ok().body(loginService.login(request));
  }

  @PostMapping("/oauth/kakao")
  public ResponseEntity<LoginResposne> kakaoLogin(
          @Valid @RequestBody OauthLoginRequest request
          ){
    return ResponseEntity.ok().body(loginService.oauthLogin(request.code(), request.redirectUri()));
  }


}

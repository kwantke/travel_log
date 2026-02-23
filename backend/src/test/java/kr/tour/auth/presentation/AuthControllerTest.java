package kr.tour.auth.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import kr.tour.auth.application.LoginService;
import kr.tour.auth.dto.response.LoginResponse;
import kr.tour.global.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;


@WebMvcTest(controllers = AuthController.class)
@DisplayName("로그인 컨트롤러")
class AuthControllerTest extends ControllerTest {

  @MockitoBean
  private LoginService loginService;


  @DisplayName("카카오 로그인 V1 - 200 OK")
  @Test
  void kakaoLoginV1_OK() throws Exception {

    var response = new LoginResponse(1L,
            "리비", "http://img-url.com",
            "access12341234.token12341234.fake-signature",
            "refresh12341234.token12341234.fake-signature");

    given(loginService.oauthLogin(any(), any())).willReturn(response);

    mockMvc.perform(post("/api/v1/login/oauth/kakao")
                    .queryParam("code", "test-code")
                    .queryParam("redirectUri", "http://localhost:3000/callback")
            )
            .andExpect(status().isOk())
            .andDo(document("v1-post-auth-login-ok",
                    resource(ResourceSnippetParameters.builder()
                            .tag("Auth API")
                            .summary("카카오 로그인 V1")
                            .description("카카오 `권한code` 와 `redirectUri` 로 로그인 또는 회원가입을 진행하여 `accessToken`과 `refreshToken`을 발급받습니다.")
                            .queryParameters(
                                    parameterWithName("code").description("카카오 권한 code"),
                                    parameterWithName("redirectUri").description("카카오 리다이렉트 URI")
                            )
                            .responseFields(
                                    fieldWithPath("memberId").description("사용자 ID"),
                                    fieldWithPath("nickname").description("닉네임"),
                                    fieldWithPath("profileImageUrl").description("프로필 이미지 URL"),
                                    fieldWithPath("accessToken").description("발급된 access token"),
                                    fieldWithPath("refreshToken").description("발급된 refresh token")
                            )
                            .build()
                    )));


  }

}
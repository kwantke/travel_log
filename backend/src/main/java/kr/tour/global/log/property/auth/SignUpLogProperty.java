package kr.tour.global.log.property.auth;


import kr.tour.global.log.LogEvent;
import kr.tour.global.log.property.LogProperty;
import kr.tour.member.domain.Member;
import kr.tour.member.domain.enums.LoginType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpLogProperty implements LogProperty {

    private final String signupProvider;
    private final String createdAt;
    private final long memberId;

    public static SignUpLogProperty ofOAuth(Member member, LoginType loginType) {
        return new SignUpLogProperty(loginType.getName(), member.getCreatedAt().toString(), member.getId());
    }

    public static SignUpLogProperty fromLocal(Member member) {
        return new SignUpLogProperty(LoginType.NONE.getName(), member.getCreatedAt().toString(),
                member.getId());
    }

    @Override
    public String getEventName() {
        return LogEvent.SIGNUP.getEventName();
    }
}

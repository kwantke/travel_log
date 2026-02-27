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
public class LoginLogProperty implements LogProperty {

    private final String loginProvider;
    private final long memberId;

    public static LoginLogProperty successLocal(Member member) {
        return new LoginLogProperty(LoginType.NONE.getName(), member.getId());
    }

    public static LoginLogProperty successOAuth(Member member, LoginType loginType) {
        return new LoginLogProperty(loginType.getName(), member.getId());
    }

    @Override
    public String getEventName() {
        return LogEvent.LOGIN.getEventName();
    }
}

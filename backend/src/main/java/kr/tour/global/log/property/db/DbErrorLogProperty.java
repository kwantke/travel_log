package kr.tour.global.log.property.db;


import kr.tour.global.log.LogEvent;
import kr.tour.global.log.property.LogProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DbErrorLogProperty implements LogProperty {

    private final String query;
    private final String targetMethod;
    private final String exceptionName;
    private final String exceptionMessage;

    public static DbErrorLogProperty of(String query, String targetMethod, String exceptionName,
                                        String exceptionMessage) {
        return new DbErrorLogProperty(query, targetMethod, exceptionName, exceptionMessage);
    }

    @Override
    public String getEventName() {
        return LogEvent.DB_ERROR.getEventName();
    }
}

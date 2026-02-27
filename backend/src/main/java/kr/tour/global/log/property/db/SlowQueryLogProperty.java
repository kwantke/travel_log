package kr.tour.global.log.property.db;

import kr.tour.global.log.LogEvent;
import kr.tour.global.log.property.LogProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SlowQueryLogProperty implements LogProperty {

    private final String query;
    private final long executionTimeMs;
    private final String targetMethod;

    public static SlowQueryLogProperty of(String query, long executionTime, String methodName) {
        return new SlowQueryLogProperty(query, executionTime, methodName);
    }

    @Override
    public String getEventName() {
        return LogEvent.DB_SLOW_QUERY.getEventName();
    }
}

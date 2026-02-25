package kr.tour.global.common;

import org.springframework.data.auditing.DateTimeProvider;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public class TestClock implements DateTimeProvider {

    private static LocalDateTime fixedTime;

    public static void freezeAt(LocalDateTime time) {
        fixedTime = time;
    }

    public static void unfreeze() {
        fixedTime = null;
    }

    @Override
    public Optional<TemporalAccessor> getNow() {
        LocalDateTime now = (fixedTime == null) ? LocalDateTime.now() : fixedTime;
        return Optional.of(now);
    }
}

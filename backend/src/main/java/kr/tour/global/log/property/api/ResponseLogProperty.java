package kr.tour.global.log.property.api;

import kr.tour.global.log.LogEvent;
import kr.tour.global.log.property.LogProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseLogProperty implements LogProperty {

    private final String endPoint;
    private final long timeTakenMs;
    private final int status;
    private final int bodySizeBytes;

    public static ResponseLogProperty of(String endPoint, ResponseEntity<?> responseEntity) {
        long timeTakenMs = calculateTimeTakenMs();
        int status = responseEntity.getStatusCode().value();
        int responseSizeBytes = getResponseBodySize(responseEntity);
        return new ResponseLogProperty(endPoint, timeTakenMs, status, responseSizeBytes);
    }

    private static int getResponseBodySize(ResponseEntity<?> responseEntity) {
        if (responseEntity.hasBody() && responseEntity.getBody() != null) {
            return responseEntity.getBody().toString().getBytes(StandardCharsets.UTF_8).length;
        }
        return 0;
    }

    private static long calculateTimeTakenMs() {
        String startTime = MDC.get("startTime");
        if (startTime == null) {
            return -1L;
        }
        try {
            return System.currentTimeMillis() - Long.parseLong(startTime);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    @Override
    public String getEventName() {
        return LogEvent.RESPONSE.getEventName();
    }
}

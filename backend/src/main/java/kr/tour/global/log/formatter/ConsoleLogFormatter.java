package kr.tour.global.log.formatter;


import kr.tour.global.log.property.api.ExceptionLogProperty;
import kr.tour.global.log.property.api.RequestLogProperty;
import kr.tour.global.log.property.api.ResponseLogProperty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsoleLogFormatter {

    private static final List<String> SENSITIVE_KEYS = List.of(
            "password",
            "accessToken",
            "refreshToken",
            "url");

    public static String formatResponseLogProperty(ResponseLogProperty responseLogProperty) {
        return String.format(
                "[%s] ← %s statusCode=%d timeTakenMs=%d responseSizeBytes=%s",
                responseLogProperty.getEventName(),
                responseLogProperty.getEndPoint(),
                responseLogProperty.getStatus(),
                responseLogProperty.getTimeTakenMs(),
                responseLogProperty.getBodySizeBytes()
        );
    }

    public static String formatRequestLogProperty(RequestLogProperty requestLogProperty) {
        String endPoint = requestLogProperty.getEndPoint();
        String method = requestLogProperty.getMethod();
        Map<String, List<String>> params = requestLogProperty.getRequestParameter();
        Object body = requestLogProperty.getRequestBody();

        return String.format("[%s] → %s params=%s body=%s",
                requestLogProperty.getEventName(),
                endPoint + " " + method,
                toFlatParamString(params),
                truncateBody(body == null ? "null" : body.toString())
        );
    }

    public static String formatExceptionLogProperty(ExceptionLogProperty exceptionLogProperty) {
        ExceptionLogProperty.ErrorDetail errorDetail = exceptionLogProperty.getErrorDetail();
        ExceptionLogProperty.Status httpStatus = exceptionLogProperty.getHttpStatus();

        return String.format("[%s] → %s exception=%s statusCode=%d, message=%s",
                exceptionLogProperty.getEventName(),
                exceptionLogProperty.getEndPoint() + " " + exceptionLogProperty.getMethod(),
                errorDetail.getExceptionName(),
                httpStatus.getCode(),
                errorDetail.getMessage()
        );
    }

    private static String toFlatParamString(Map<String, List<String>> params) {
        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", ", "{", "}"));
    }

    private static String truncateBody(String rawBody) {
        String masked = maskBodySensitiveData(rawBody);
        return masked.length() > 200 ? masked.substring(0, 200) + "...(생략)" : masked;
    }

    private static String maskBodySensitiveData(String raw) {
        for (String key : SENSITIVE_KEYS) {
            // record toString style: ClassName[field1=value1, key=value2, ...]
            String regex = String.format("(?i)(%s=)([^,\\]]+)", key);
            raw = raw.replaceAll(regex, "$1****");
        }
        return raw;
    }
}

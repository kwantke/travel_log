package kr.tour.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(CoreException exception) {
        log.warn("CoreException :: message = {}", exception.getMessage());

        ExceptionResponse data = new ExceptionResponse(exception.getMessage());
        return ResponseEntity.status(exception.getErrorCode().getHttpStatus())
                .body(data);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        log.warn("METHOD_ARGUMENT_NOT_VALID_EXCEPTION :: message = {}", exception.getMessage());

        String message = exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        ExceptionResponse data = new ExceptionResponse(message);
        return ResponseEntity.badRequest()
                .body(data);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        log.error("EXCEPTION :: stackTrace = ", exception);

        ExceptionResponse data = new ExceptionResponse("서버에 문제가 발생했습니다. 투룻에 문의해 주세요.");
        return ResponseEntity.internalServerError()
                .body(data);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorized(UnauthorizedException exception) {
        log.warn("UNAUTHORIZED_EXCEPTION :: message = {}", exception.getMessage());

        ExceptionResponse data = new ExceptionResponse(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(data);
    }

}

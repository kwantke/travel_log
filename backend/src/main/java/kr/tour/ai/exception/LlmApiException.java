package kr.tour.ai.exception;

import kr.tour.global.exception.CoreException;
import kr.tour.global.exception.ErrorCode;

public class LlmApiException extends CoreException {
  public LlmApiException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public LlmApiException(ErrorCode errorCode) {
    super(errorCode);
  }

  public LlmApiException(String message) {
    super(LlmApiErrorCode.AI_API_FAILED, message);
  }

  public LlmApiException(String message, Throwable cause) {
    super(LlmApiErrorCode.AI_API_FAILED, message);
    initCause(cause);
  }

  public LlmApiException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message);
    initCause(cause);
  }


}

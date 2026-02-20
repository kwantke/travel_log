package kr.tour.global.exception;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException{

  private final ErrorCode errorCode;
  private final String message;

  public CoreException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
    this.message = message;
  }

  public CoreException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
  }
}

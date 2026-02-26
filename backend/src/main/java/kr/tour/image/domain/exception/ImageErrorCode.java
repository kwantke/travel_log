package kr.tour.image.domain.exception;

import kr.tour.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ImageErrorCode implements ErrorCode {
  INVALID_FILE(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다"),
  INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "파일 형식이 잘못되었습니다"),
  NOT_SUPPORT_FILE_TYPE(HttpStatus.BAD_REQUEST, "파일 형식이 잘못 되었습니다"),
  EMPTY_FILE_NAME(HttpStatus.BAD_REQUEST, "파일 이름은 비어 있을 수 없습니다"),
  INVALID_FILE_URL(HttpStatus.BAD_REQUEST, "S3 이미지 url 형식이 잘못되었습니다"),

  // S3
  ERROR_UPLOAD_S3_IMAGE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "S3에 이미지를 업로드중 오류가 발생했습니다"),
  NOT_FOUND_S3_IMAGE_FILE(HttpStatus.NOT_FOUND, "S3 버킷에 복사하려는 사진이 존재하지 않습니다."),

  ;
  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public String getCode() {
    return this.name();
  }
}

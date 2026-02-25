package kr.tour.image.domain;

import kr.tour.global.exception.CoreException;
import kr.tour.image.domain.exception.ImageErrorCode;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ImageFile {
  private static final List<String> WHITE_LIST = List.of("jpg", "jpeg", "png", "webp", "heic");

  private final MultipartFile file;

  public ImageFile(MultipartFile file) {
    validation(file);
    this.file = file;
  }

  private void validation(MultipartFile file) {
    validateNotNull(file);
    String fileName = file.getOriginalFilename();
    validateFileNameNotBlank(fileName);
    validateExtension(fileName);
  }

  private void validateNotNull(MultipartFile file) {
    if (file == null) {
      throw new CoreException(ImageErrorCode.INVALID_FILE);
    }
  }

  private void validateFileNameNotBlank(String fileName) {
    if (fileName == null || fileName.isBlank()) {
      throw new CoreException(ImageErrorCode.EMPTY_FILE_NAME);
    }
  }

  private void validateExtension(String fileName) {
    int extensionIndex = fileName.lastIndexOf(".");
    if (extensionIndex == -1 || fileName.endsWith(".")) {
      throw new CoreException(ImageErrorCode.EMPTY_FILE_NAME);
    }
    String extension = fileName.substring(extensionIndex + 1);
    if (!WHITE_LIST.contains(extension.toLowerCase())) {
      throw new CoreException(ImageErrorCode.NOT_SUPPORT_FILE_TYPE,"지원하지 않는 확장자입니다: " + extension);
    }
  }
}

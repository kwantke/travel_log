package kr.tour.image.presentation;

import kr.tour.image.application.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/image")
public class ImageController {
  private final ImageService imageService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<List<String>> uploadImages(List<MultipartFile> files) {
    List<String> imageUrls = imageService.uploadImages(files);
    return ResponseEntity.ok(imageUrls);
  }

}

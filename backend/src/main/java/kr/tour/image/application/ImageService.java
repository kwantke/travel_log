package kr.tour.image.application;

import kr.tour.image.domain.ImageFile;
import kr.tour.image.infrastructure.AwsS3Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {

  private final AwsS3Provider s3Provider;

  public List<String> uploadImages(List<MultipartFile> files) {
    List<ImageFile> imageFiles = files.stream()
            .map(ImageFile::new)
            .toList();
    return s3Provider.uploadImages(imageFiles);
  }
}

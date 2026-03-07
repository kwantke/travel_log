package kr.tour.image.infrastructure;

import kr.tour.global.exception.CoreException;
import kr.tour.image.domain.ImageFile;
import kr.tour.image.domain.exception.ImageErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class AwsS3Provider {

  @Value("${spring.profiles.active:}")
  private String activeProfile;

  private final S3Client s3Client;
  private final String bucket;
  private final String imageBaseUri;
  private final String originStoragePath;
  private final String temporaryStoragePath;
  private final String imageStoragePath;

  public AwsS3Provider(
          S3Client s3Client,
          @Value("${cloud.aws.s3.bucket}") String bucket,
          @Value("${cloud.aws.s3.image-base-uri}") String imageBaseUri,
          @Value("${cloud.aws.s3.origin-storage-path}") String originStoragePath,
          @Value("${cloud.aws.s3.temporary-storage-path}") String temporaryStoragePath,
          @Value("${cloud.aws.s3.image-storage-path}") String imageStoragePath
  ) {
    this.s3Client = s3Client;
    this.bucket = bucket;
    this.imageBaseUri = imageBaseUri;
    this.originStoragePath = originStoragePath;
    this.temporaryStoragePath = temporaryStoragePath;
    this.imageStoragePath = imageStoragePath;
  }

  public List<String> uploadImages(List<ImageFile> files) {
    List<String> urls = new ArrayList<>();

    files.stream()
            .map(ImageFile::getFile)
            .forEach(file ->{
              String newFileName = createNewFileName(file.getOriginalFilename());
              String filePath = temporaryStoragePath + newFileName;
              uploadFile(file, filePath, s3Client);
              String s3Key = imageBaseUri + temporaryStoragePath + newFileName;
              urls.add(s3Key);
            });

    return urls;
  }

  private void uploadFile(MultipartFile file, String filePath, S3Client s3Client) {
    try {
      RequestBody requestBody = RequestBody.fromBytes(file.getBytes());
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
              .bucket(bucket)
              .key(filePath)
              .contentType(file.getContentType())
              .contentLength(file.getSize())
              .build();

      s3Client.putObject(putObjectRequest, requestBody);
    } catch (IOException exception) {
      throw new CoreException(ImageErrorCode.ERROR_UPLOAD_S3_IMAGE_FILE);
    }
  }

  private String createNewFileName(String fileName) {
    return UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
  }

  public String copyImageToPermanentStorage(String imageUrl) {
    if (isInPermanentStorage(imageUrl)) {
      return imageUrl;
    }

    validateS3Path(imageUrl);
    String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    String sourceKey = originStoragePath + temporaryStoragePath + fileName;
    String destinationKey = sourceKey.replace(temporaryStoragePath, imageStoragePath);
    if (!activeProfile.contains("local")) {
      copyFile(sourceKey, destinationKey);
    }
    return imageUrl.replace(temporaryStoragePath, imageStoragePath);
  }

  private boolean isInPermanentStorage(String imageUrl) {
    return imageUrl.startsWith(imageBaseUri + imageStoragePath);
  }

  private void validateS3Path(String imageKey) {
    if (!imageKey.startsWith(imageBaseUri + temporaryStoragePath)) {
      throw new CoreException(ImageErrorCode.INVALID_FILE_URL);
    }
  }

  private void copyFile(String sourceKey, String destinationKey) {
    try {
      CopyObjectRequest request = CopyObjectRequest.builder()
              .sourceBucket(bucket)
              .sourceKey(sourceKey)
              .destinationBucket(bucket)
              .destinationKey(destinationKey)
              .build();

      s3Client.copyObject(request);
    } catch (NoSuchKeyException exception) {
      throw new CoreException(ImageErrorCode.NOT_FOUND_S3_IMAGE_FILE);
    }
  }

}

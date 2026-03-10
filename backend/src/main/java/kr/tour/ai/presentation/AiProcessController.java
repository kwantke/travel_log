package kr.tour.ai.presentation;

import jakarta.validation.Valid;
import kr.tour.ai.application.AiProcessingService;
import kr.tour.ai.dto.request.TravelogTagGenerateRequest;
import kr.tour.ai.dto.response.TravelogTagGenerateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiProcessController {

  private final AiProcessingService travelTagAiService;

  @PostMapping("/generate/tags")
  public ResponseEntity<TravelogTagGenerateResponse> generateTravelTags(
          @Valid @RequestBody TravelogTagGenerateRequest request
  ) {
    TravelogTagGenerateResponse response = travelTagAiService.generateTags(request.content());
    return ResponseEntity.ok(response);
  }
}
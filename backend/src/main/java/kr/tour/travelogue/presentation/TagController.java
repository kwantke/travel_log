package kr.tour.travelogue.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.tour.travelogue.application.TagService;
import kr.tour.travelogue.dto.response.TagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "태그")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "모든 태그 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "태그가 조회가 정상적으로 성공했을 때"
            )
    })
    @GetMapping
    public ResponseEntity<List<TagResponse>> readTags() {
        List<TagResponse> data = tagService.readTags();
        return ResponseEntity.ok(data);
    }
}

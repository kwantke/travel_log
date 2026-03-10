package kr.tour.ai.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TravelogTagGenerateRequest(
        @NotBlank(message = "여행기 내용은 비어 있을 수 없습니다.")
        String content
) {
}
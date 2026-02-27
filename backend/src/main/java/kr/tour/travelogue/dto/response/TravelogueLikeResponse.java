package kr.tour.travelogue.dto.response;

public record TravelogueLikeResponse(
        Boolean isLiked,
        Long likeCount

) {
}

package kr.tour.travelogue.application;

import kr.tour.member.domain.Member;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueLike;
import kr.tour.travelogue.infrastructure.TravelogueLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TravelogueLikeService {

  private final TravelogueLikeRepository travelogueLikeRepository;

  public boolean existByTravelogueAndMember(Travelogue travelogue, Member liker) {
    return travelogueLikeRepository.existsByTravelogueAndLiker(travelogue, liker);
  }

  @Transactional
  public void likeTravelogue(Travelogue travelogue, Member liker) {
    boolean notExists = !travelogueLikeRepository.existsByTravelogueAndLiker(travelogue, liker);
    if (notExists) {
      TravelogueLike travelogueLike = new TravelogueLike(travelogue, liker);
      travelogueLikeRepository.save(travelogueLike);
      travelogue.increaseLikeCount();
    }
  }
}

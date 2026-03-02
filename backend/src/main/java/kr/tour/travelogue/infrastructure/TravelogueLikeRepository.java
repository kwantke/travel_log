package kr.tour.travelogue.infrastructure;

import kr.tour.member.domain.Member;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelogueLikeRepository extends JpaRepository<TravelogueLike, Long> {

  boolean existsByTravelogueAndLiker(Travelogue travelogue, Member liker);

  void deleteAllByTravelogue(Travelogue travelogue);

  void deleteByTravelogueAndLiker(Travelogue travelogue, Member liker);
}

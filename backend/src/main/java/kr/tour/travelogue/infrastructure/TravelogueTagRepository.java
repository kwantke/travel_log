package kr.tour.travelogue.infrastructure;

import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelogueTagRepository extends JpaRepository<TravelogueTag, Long> {
  void deleteAllByTravelogue(Travelogue travelogue);

  List<TravelogueTag> findAllByTravelogue(Travelogue travelogue);
}

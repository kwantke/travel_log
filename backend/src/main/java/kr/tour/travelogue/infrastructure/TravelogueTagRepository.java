package kr.tour.travelogue.infrastructure;

import kr.tour.travelogue.domain.TravelogueTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelogueTagRepository extends JpaRepository<TravelogueTag, Long> {
}

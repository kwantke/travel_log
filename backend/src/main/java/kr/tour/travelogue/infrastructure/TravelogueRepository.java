package kr.tour.travelogue.infrastructure;

import kr.tour.travelogue.domain.Travelogue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelogueRepository extends JpaRepository<Travelogue, Long> {


}

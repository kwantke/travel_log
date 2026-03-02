package kr.tour.travelogue.infrastructure;

import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueCountry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelogueCountryRepository extends JpaRepository<TravelogueCountry, Long> {
  void deleteAllByTravelogue(Travelogue travelogue);
}

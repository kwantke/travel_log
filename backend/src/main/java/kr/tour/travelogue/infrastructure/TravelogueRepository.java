package kr.tour.travelogue.infrastructure;

import kr.tour.travelogue.domain.Travelogue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelogueRepository extends JpaRepository<Travelogue, Long> {

  @Override
  @EntityGraph(attributePaths = {"author"})
  Page<Travelogue> findAll(Pageable pageable);

  @EntityGraph(attributePaths = {
          "author",
          "travelogueDays",
          "travelogueDays.traveloguePlaces",
          "travelogueDays.traveloguePlaces.traveloguePhotos",
          "travelogueDays.traveloguePlaces.place" // 만약 Place 정보도 필요하다면 추가
  })
  Optional<Travelogue> findById(Long id);
}

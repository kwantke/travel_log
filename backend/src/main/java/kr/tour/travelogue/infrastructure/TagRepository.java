package kr.tour.travelogue.infrastructure;


import kr.tour.travelogue.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}

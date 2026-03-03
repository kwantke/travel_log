package kr.tour.travelogue.application;

import kr.tour.global.exception.CoreException;
import kr.tour.travelogue.domain.Tag;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueTag;
import kr.tour.travelogue.domain.exception.TravelogueErrorCode;
import kr.tour.travelogue.infrastructure.TagRepository;
import kr.tour.travelogue.infrastructure.TravelogueTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TravelogueTagService {

  private final TagRepository tagRepository;
  private final TravelogueTagRepository travelogueTagRepository;

  @Transactional
  public List<TravelogueTag> createTravelogueTags(Travelogue travelogue, List<Long> tagIds) {
    return tagIds.stream()
            .map(id -> {
              Tag tag = getTagById(id);
              return travelogueTagRepository.save(new TravelogueTag(travelogue, tag));
            }).toList();
  }

  @Transactional(readOnly = true)
  public Tag getTagById(Long id) {
    return tagRepository.findById(id)
            .orElseThrow(() -> new CoreException(TravelogueErrorCode.INVALID_TAG));
  }

  @Transactional
  public List<TravelogueTag> updateTravelogueTag(Travelogue travelogue, List<Long> tags) {
    deleteAllByTravelogue(travelogue);
    return createTravelogueTags(travelogue, tags);

  }

  @Transactional
  public void deleteAllByTravelogue(Travelogue travelogue) {
    travelogueTagRepository.deleteAllByTravelogue(travelogue);
  }
}

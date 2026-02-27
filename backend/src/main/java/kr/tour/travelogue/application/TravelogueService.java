package kr.tour.travelogue.application;

import kr.tour.global.exception.CoreException;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueFilterCondition;
import kr.tour.travelogue.domain.exception.TravelogueErrorCode;
import kr.tour.travelogue.domain.search.SearchCondition;
import kr.tour.travelogue.infrastructure.TravelogueRepository;
import kr.tour.travelogue.infrastructure.query.TravelogueQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TravelogueService {

  private final TravelogueRepository travelogueRepository;
  private final TravelogueQueryRepository travelogueQueryRepository;

  @Transactional(readOnly = true)
  public Page<Travelogue> findAll(
          SearchCondition searchCondition,
          TravelogueFilterCondition filter,
          Pageable pageable
  ){
    if(searchCondition.isNoneCountry()){
      return Page.empty();
    }

    if (filter.isEmptyCondition() && searchCondition.isEmptyCondition()) {
      return travelogueRepository.findAll(pageable);
    }

    return travelogueQueryRepository.findAllByCondition(searchCondition, filter, pageable);
  }

  @Transactional
  public Travelogue save(Travelogue travelogue) {
    return travelogueRepository.save(travelogue);
  }

  @Transactional(readOnly = true)
  public Travelogue getTravelogueById(Long id) {
    return travelogueRepository.findById(id)
            .orElseThrow(() -> new CoreException(TravelogueErrorCode.INVALID_TRAVELOGUE));
  }
}

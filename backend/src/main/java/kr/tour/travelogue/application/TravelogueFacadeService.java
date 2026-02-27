package kr.tour.travelogue.application;

import kr.tour.global.dto.MemberAuth;
import kr.tour.member.application.MemberService;
import kr.tour.member.domain.Member;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueFilterCondition;
import kr.tour.travelogue.domain.search.SearchCondition;
import kr.tour.travelogue.dto.request.TravelogueFilterRequest;
import kr.tour.travelogue.dto.request.TravelogueRequest;
import kr.tour.travelogue.dto.request.TravelogueSearchRequest;
import kr.tour.travelogue.dto.response.TravelogueCreateResponse;
import kr.tour.travelogue.dto.response.TravelogueLikeResponse;
import kr.tour.travelogue.dto.response.TravelogueResponse;
import kr.tour.travelogue.dto.response.TravelogueSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TravelogueFacadeService {

  private final TravelogueService travelogueService;
  private final TravelogueTagService travelogueTagService;
  private final TravelogueCountryService travelogueCountryService;
  private final TravelogueImagePerpetuationService travelogueImagePerpetuationService;
  private final TravelogueLikeService travelogueLikeService;
  private final MemberService memberService;

  @Transactional(readOnly = true)
  public Page<TravelogueSimpleResponse> findSimpleTravelogues(
          TravelogueFilterRequest filterRequest,
          TravelogueSearchRequest searchRequest,
          Pageable pageable
  ) {
    TravelogueFilterCondition filter = filterRequest.toFilterCondition();
    SearchCondition searchCondition = searchRequest.toSearchCondition();

    Page<Travelogue> travelogues = travelogueService.findAll(searchCondition, filter, pageable);

    return travelogues.map(TravelogueSimpleResponse::from);
  }

  @Transactional
  public TravelogueCreateResponse createTravelogue(MemberAuth member, TravelogueRequest request) {
    Member author = memberService.getMemberById(member.memberId());
    Travelogue travelogue = travelogueService.save(request.toTravelogue(author));
    travelogueImagePerpetuationService.copyTravelogueImagesToPermanentStorage(travelogue);
    travelogueTagService.createTravelogueTags(travelogue, request.tags());
    travelogueCountryService.createTravelogueCountries(travelogue, request);

    return TravelogueCreateResponse.from(travelogue);
  }


  public TravelogueResponse findTravelogueByIdForGuest(Long id) {
    Travelogue travelogue = travelogueService.getTravelogueById(id);

    return TravelogueResponse.createResponseForGuest(travelogue);
  }

  @Transactional(readOnly = true)
  public TravelogueResponse findTravelogueByIdForAuthenticated(Long id, MemberAuth member) {
    Member accessor = memberService.getMemberById(member.memberId());
    Travelogue travelogue = travelogueService.getTravelogueById(id);
    boolean likeFromAccessor = travelogueLikeService.existByTravelogueAndMember(travelogue, accessor);

    return TravelogueResponse.of(travelogue, likeFromAccessor);
  }

  @Transactional
  public TravelogueLikeResponse likeTravelogue(Long travelogueId, MemberAuth member) {
    Travelogue travelogue = travelogueService.getTravelogueById(travelogueId);
    Member liker = memberService.getMemberById(member.memberId());
    travelogueLikeService.likeTravelogue(travelogue, liker);

    return new TravelogueLikeResponse(true, travelogue.getLikeCount());
  }
}

package kr.tour.travelogue.application;

import kr.tour.global.dto.MemberAuth;
import kr.tour.member.application.MemberService;
import kr.tour.member.domain.Member;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueFilterCondition;
import kr.tour.travelogue.domain.TravelogueTag;
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

import java.util.List;

@RequiredArgsConstructor
@Service
public class TravelogueFacadeService {

  private final TravelogueService travelogueService;
  private final TravelogueTagService travelogueTagService;
  private final TravelogueCountryService travelogueCountryService;
  private final TravelogueImagePerpetuationService travelogueImagePerpetuationService;
  private final TravelogueLikeService travelogueLikeService;
  private final MemberService memberService;
  //private final RecommendationService recommendationService;

  @Transactional(readOnly = true)
  public Page<TravelogueSimpleResponse> findSimpleTravelogues(
          TravelogueFilterRequest filterRequest,
          TravelogueSearchRequest searchRequest,
          Pageable pageable
  ) {
    TravelogueFilterCondition filter = filterRequest.toFilterCondition();
    SearchCondition searchCondition = searchRequest.toSearchCondition();

    Page<Travelogue> travelogues = travelogueService.findAll(searchCondition, filter, pageable);

    return travelogues.map(this::getTravelogueSimpleResponse);
  }

  private TravelogueSimpleResponse getTravelogueSimpleResponse(Travelogue travelogue) {
    List<TravelogueTag> travelogueTags = travelogueTagService.readTagByTravelogue(travelogue);

    return TravelogueSimpleResponse.of(travelogue, travelogueTags);
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
    List<TravelogueTag> travelogueTags = travelogueTagService.readTagByTravelogue(travelogue);
    return TravelogueResponse.createResponseForGuest(travelogue, travelogueTags);
  }

  @Transactional(readOnly = true)
  public TravelogueResponse findTravelogueByIdForAuthenticated(Long id, MemberAuth member) {
    Member accessor = memberService.getMemberById(member.memberId());
    Travelogue travelogue = travelogueService.getTravelogueById(id);
    List<TravelogueTag> travelogueTags = travelogueTagService.readTagByTravelogue(travelogue);
    boolean likeFromAccessor = travelogueLikeService.existByTravelogueAndMember(travelogue, accessor);

    //recommendationService.saveUserRecommendTag(member.memberId(), travelogue.getTravelogueTags());
    return TravelogueResponse.of(travelogue, travelogueTags, likeFromAccessor);
  }

  @Transactional
  public TravelogueLikeResponse likeTravelogue(Long travelogueId, MemberAuth member) {
    Travelogue travelogue = travelogueService.getTravelogueById(travelogueId);
    Member liker = memberService.getMemberById(member.memberId());
    travelogueLikeService.likeTravelogue(travelogue, liker);

    return new TravelogueLikeResponse(true, travelogue.getLikeCount());
  }

  @Transactional
  public TravelogueResponse updateTravelogue(Long id, MemberAuth member, TravelogueRequest updateRequest) {
    Member author = memberService.getMemberById(member.memberId());

    Travelogue updated = travelogueService.update(id, author, updateRequest);
    travelogueImagePerpetuationService.copyTravelogueImagesToPermanentStorage(updated);
    List<TravelogueTag> travelogueTags = travelogueTagService.updateTravelogueTag(updated, updateRequest.tags());
    travelogueCountryService.updateTravelogueCountries(updated, updateRequest);
    boolean isLikedFromAccessor = travelogueLikeService.existByTravelogueAndMember(updated, author);
    return  TravelogueResponse.of(updated, travelogueTags, isLikedFromAccessor);

  }
  @Transactional
  public void deleteTravelogueById(Long id, MemberAuth member) {
    Member author = memberService.getMemberById(member.memberId());
    Travelogue travelogue = travelogueService.getTravelogueById(id);

    travelogueTagService.deleteAllByTravelogue(travelogue);
    travelogueLikeService.deleteAllByTravelogue(travelogue);
    travelogueCountryService.deleteAllByTravelogue(travelogue);
    travelogueService.delete(travelogue, author);

  }

  @Transactional
  public TravelogueLikeResponse unlikeTravelogue(Long travelogueId, MemberAuth member) {
    Travelogue travelogue = travelogueService.getTravelogueById(travelogueId);
    Member liker = memberService.getMemberById(member.memberId());
    travelogueLikeService.unlikeTravelogue(travelogue, liker);

    return new TravelogueLikeResponse(false, travelogue.getLikeCount());
  }
}

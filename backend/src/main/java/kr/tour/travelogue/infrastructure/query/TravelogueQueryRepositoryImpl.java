package kr.tour.travelogue.infrastructure.query;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.tour.travelogue.domain.QTravelogueTag;
import kr.tour.travelogue.domain.Travelogue;
import kr.tour.travelogue.domain.TravelogueFilterCondition;
import kr.tour.travelogue.domain.enums.CountryCode;
import kr.tour.travelogue.domain.search.SearchCondition;
import kr.tour.travelogue.domain.search.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


import static kr.tour.travelogue.domain.QTravelogue.travelogue;
import static kr.tour.travelogue.domain.QTravelogueCountry.travelogueCountry;
import static kr.tour.travelogue.domain.QTravelogueTag.travelogueTag;

@RequiredArgsConstructor
@Repository
public class TravelogueQueryRepositoryImpl implements TravelogueQueryRepository{

  public static final String BLANK = " ";
  public static final String EMPTY = "";
  public static final String TEMPLATE = "replace({0}, ' ', '')";

  private final JPAQueryFactory jpaQueryFactory;
  @Override
  public Page<Travelogue> findAllByCondition(
          SearchCondition searchCondition,
          TravelogueFilterCondition filterCondition,
          Pageable pageable) {

    JPAQuery<Travelogue> baseQuery = jpaQueryFactory.selectFrom(travelogue);

    addSearchCondition(baseQuery, searchCondition);
    addFilterCondition(baseQuery, filterCondition);

    List<Travelogue> results = baseQuery.orderBy(toOrderSpecifiers(pageable))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    return new PageImpl<>(results, pageable, results.size());
  }



  private void addSearchCondition(JPAQuery<Travelogue> baseQuery, SearchCondition condition) {
    String keyword = condition.getKeyword();

    if (condition.getSearchType() == SearchType.COUNTRY) {
      CountryCode countryCode = CountryCode.findByName(keyword);
      findByCountryCode(baseQuery, countryCode);
      return;
    }

    if (condition.getSearchType() == SearchType.AUTHOR || condition.getSearchType() == SearchType.TITLE) {
      findByTitleOrAuthor(condition, baseQuery, keyword);
    }
  }

  private void findByTitleOrAuthor(SearchCondition condition, JPAQuery<Travelogue> baseQuery, String keyword) {
    baseQuery.where(Expressions.stringTemplate(TEMPLATE, getTargetField(condition.getSearchType()))
                    .containsIgnoreCase(keyword.replace(BLANK, EMPTY)))
            .orderBy(travelogue.id.desc());
  }

  private StringPath getTargetField(SearchType searchType) {
    if (SearchType.AUTHOR.equals(searchType)) {
      return travelogue.author.nickname;
    }
    return travelogue.title;
  }

  private void findByCountryCode(JPAQuery<Travelogue> baseQuery, CountryCode countryCode) {
    baseQuery.join(travelogueCountry)
            .on(travelogue.id.eq(travelogueCountry.travelogue.id))
            .where(travelogueCountry.countryCode.eq(countryCode));

  }

  private void addFilterCondition(JPAQuery<Travelogue> baseQuery, TravelogueFilterCondition filterCondition) {
    addTagFilter(baseQuery, filterCondition);
  }

  private void addTagFilter(JPAQuery<Travelogue> baseQuery, TravelogueFilterCondition filterCondition) {
    if (filterCondition.isEmptyTagCondition()) {
      return;
    }

    List<Long> tagIds = filterCondition.getTag();
    //tagIds.forEach(tagId -> joinTravelogueTag(baseQuery, tagId));
    for (Long tagId : tagIds) {
      baseQuery.where(existsTag(tagId));
    }

  }
  private BooleanExpression existsTag(Long tagId) {
    // 서브쿼리용 별칭(중요: 메인에서 쓰는 travelogueTag static과 겹치지 않게)
    QTravelogueTag tt = new QTravelogueTag("tt_" + tagId);

    return JPAExpressions
            .selectOne()
            .from(tt)
            .where(
                    tt.travelogue.eq(travelogue)
                            .and(tt.tag.id.eq(tagId))
            )
            .exists();
  }
  private void joinTravelogueTag(JPAQuery<Travelogue> query, Long tagId) {
    QTravelogueTag travelogueTag = new QTravelogueTag("travelogueTag" + tagId);
    query.join(travelogueTag)
            .on(travelogueTag.travelogue.eq(travelogue)
                    .and(travelogueTag.tag.id.eq(tagId)));
  }

  private OrderSpecifier<?>[] toOrderSpecifiers(Pageable pageable) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();

    for (Sort.Order sortOrder : pageable.getSort()) {
      String property = sortOrder.getProperty();
      Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;

      switch (property) {
        case "id" -> orders.add(new OrderSpecifier<>(direction, travelogue.id));
        case "createdAt" -> orders.add(new OrderSpecifier<>(direction, travelogue.createdAt));
        case "likeCount" -> orders.add(new OrderSpecifier<>(direction, travelogue.likeCount));
        default -> {
          // 이외의 정렬 요청 키워드는 무시합니다.
        }
      }
    }

    if (orders.isEmpty()) {
      orders.add(travelogue.createdAt.desc());
    }

    return orders.toArray(OrderSpecifier[]::new);

  }
}

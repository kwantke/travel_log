import { Text } from "@components/common";
import * as S from "./MainPage.styled";
import { FORM_VALIDATIONS_MAP } from "@constants/formValidation";
import Chip from "@components/common/Chip/Chip";
import {
  SORTING_OPTIONS_MAP,
  TRAVEL_PERIOD_OPTIONS_MAP,
} from "./MainPage.constants";
import useMultiSelectionTag from "@hooks/useMultiSelectionTag";
import { STORAGE_KEYS_MAP } from "@constants/storage";
import useSingleSelectionTag from "@hooks/useSingleSelectionTag";
import { useEffect, useMemo, useState } from "react";
import { useDragScroll } from "@hooks/useDragScroll";
import useKeyDown from "@hooks/useKeyDown/useKeyDown";
import { removeEmoji } from "@utils/removeEmojis";
import VisuallyHidden from "@components/common/VisuallyHidden/VisuallyHidden";

export default function MainPage() {
  const {
    selectedTagIDs,
    sortedTags,
    handleClickTag,
    multiSelectionTagAnimationKey,
    resetMultiSelectionTag,
  } = useMultiSelectionTag(STORAGE_KEYS_MAP.mainPageSelectedTagIDs);

  const {
    sorting,
    travelPeriod,
    resetSingleSelectionTags,
    singleSelectionAnimationKey,
    increaseSingleSelectionAnimationKey,
  } = useSingleSelectionTag(
    STORAGE_KEYS_MAP.mainPageSort,
    STORAGE_KEYS_MAP.mainPageTravelPeriod,
  );

  const isTagsSelected = useMemo(() => {
    return (
      selectedTagIDs.length !== 0 ||
      sorting.selectedOption !== "likeCount" ||
      travelPeriod.selectedOption !== ""
    );
  }, [selectedTagIDs, sorting.selectedOption, travelPeriod.selectedOption]);

  useEffect(() => {
    increaseSingleSelectionAnimationKey();
  }, [isTagsSelected, increaseSingleSelectionAnimationKey]);
  const handleClickResetButton = () => {
    resetMultiSelectionTag();
    resetSingleSelectionTags();
  };

  const { scrollRef, handleMouseUp, handleMouseDown, handleMouseMove } =
    useDragScroll<HTMLUListElement>();

  const { modalRef, handleKeyDown } = useKeyDown<HTMLElement>({
    isOpen: true,
    direction: "horizontal",
    useDynamicObserver: true,
  });

  type RefType<T> =
    | React.RefObject<T>
    | React.MutableRefObject<T>
    | React.RefCallback<T>;

  function combineRefs<T>(...refs: RefType<T>[]) {
    return (element: T | null) => {
      refs.forEach((ref) => {
        if (typeof ref === "function") {
          ref(element);
        } else if (ref != null) {
          (ref as React.MutableRefObject<T | null>).current = element;
        }
      });
    };
  }

  const combinedTagsContainerRef = combineRefs(scrollRef, modalRef);

  const [tagSelectionAnnouncement, setTagSelectionAnnouncement] = useState("");

  return (
    <>
      <S.FixedLayout>
        <S.TitleContainer>
          <Text textType="title">지금 뜨고 있는 여행기</Text>
          <Text textType="detail" css={S.subTitleStyle}>
            다른 이들의 여행을 구경해보세요. (태그는 최대{" "}
            {FORM_VALIDATIONS_MAP.tags.maxCount})
          </Text>
        </S.TitleContainer>

        <S.TagsContainer>
          <S.SingleSelectionTagsContainer>
            {isTagsSelected && (
              <Chip
                key={`reset-${singleSelectionAnimationKey}`}
                label={`초기화`}
                isSelected={false}
                onClick={handleClickResetButton}
                iconPosition="left"
                iconType="reset-icon"
              />
            )}
            <Chip
              as="button"
              aria-label="여행기 정렬"
              key={`sorting-${singleSelectionAnimationKey}`}
              label={SORTING_OPTIONS_MAP[sorting.selectedOption]}
              isSelected={true}
              iconPosition="left"
              iconType="sort-icon"
            />
            <Chip
              as="button"
              aria-label="여행기 필터"
              key={`travelPeriod-${singleSelectionAnimationKey}`}
              label={
                travelPeriod.selectedOption
                  ? TRAVEL_PERIOD_OPTIONS_MAP[travelPeriod.selectedOption]
                  : "여행 기간"
              }
              iconPosition="right"
              isSelected={travelPeriod.selectedOption !== ""}
              onClick={travelPeriod.handleOpenModal}
            />
          </S.SingleSelectionTagsContainer>

          <VisuallyHidden aria-live="assertive">
            {tagSelectionAnnouncement}
          </VisuallyHidden>
          <S.MultiSelectionTagsContainer
            ref={combinedTagsContainerRef}
            onMouseDown={handleMouseDown}
            onMouseUp={handleMouseUp}
            onMouseMove={handleMouseMove}
            onMouseLeave={handleMouseUp}
            onKeyDown={handleKeyDown}
          >
            {sortedTags.map((tag, index) => {
              const isSelected = selectedTagIDs.includes(tag.id);
              const tagName = removeEmoji(tag.tag);

              return (
                <li key={`${tag.id}-${multiSelectionTagAnimationKey}`}>
                  <Chip
                    as="button"
                    key={`${tag.id}-${multiSelectionTagAnimationKey}`}
                    index={index}
                    label={tag.tag}
                    isSelected={isSelected}
                    onClick={() => {
                      handleClickTag(tag.id);
                      setTagSelectionAnnouncement(
                        isSelected
                          ? `${tagName} 태그가 선택 해제되었습니다.`
                          : `${tagName} 태그가 선택되었습니다.`,
                      );
                    }}
                    aria-label={`${tagName} 태그`}
                  />
                </li>
              );
            })}
          </S.MultiSelectionTagsContainer>
        </S.TagsContainer>
      </S.FixedLayout>

      <S.MainPageLayout>
        
        <S.MainPageTraveloguesList>
          <S.MainPageList></S.MainPageList>
        </S.MainPageTraveloguesList>
      </S.MainPageLayout>
    </>
  );
}

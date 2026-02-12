import { Text } from "@components/common";
import * as S from "./MainPage.styled";
import { FORM_VALIDATIONS_MAP } from "@constants/formValidation";
import Chip from "@components/common/Chip/Chip";
import { SORTING_OPTIONS_MAP } from "./MainPage.constants";

export default function MainPage() {
  return (
    <>
      <S.FixedLayout>
        <S.TitleContainer>
          <Text textType="title">지금 뜨고 있는 여행기</Text>
          <Text textType="detail" css={S.subTitleStyle}>
            다른 이들의 여행을 구경해보세요. (태그는 최대 {FORM_VALIDATIONS_MAP.tags.maxCount})
          </Text>
        </S.TitleContainer>

        <S.TagsContainer>
          <S.SingleSelectionTagsContainer>
            <Chip 
            as="button"
            aria-label="여행기 정렬"
            key={`sorting-`}
            label={SORTING_OPTIONS_MAP["likeCount"]}
            />
          </S.SingleSelectionTagsContainer>

          <S.MainPageLayout>
            <S.MainPageTraveloguesList>
              <S.MainPageList>
                
              </S.MainPageList>
            </S.MainPageTraveloguesList>
          </S.MainPageLayout>
        </S.TagsContainer>
      </S.FixedLayout>
    </>
  );
}

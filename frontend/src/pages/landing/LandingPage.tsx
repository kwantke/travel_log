import FirstPage from "./firstPage/FirstPage";
import * as S from "./LandingPage.styled";
export default function LandingPage() {
  return (
    <>
      <S.Layout>
        <S.PageWrapper>
          <div css={S.firstPageStyle}>
            <FirstPage />
          </div>
        </S.PageWrapper>
      </S.Layout>
    </>
  );
}

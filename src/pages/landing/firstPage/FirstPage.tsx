import { useNavigate } from "react-router";
import * as S from "./FirstPage.styled";
import { ROUTE_PATHS_MAP } from "@constants/route";
import { createAnimation } from "../Animation.styled";
import useAnimationObserver from "../hook/useAnimationObserver";
import Text from "@components/Text/Text";
import { BigTturi } from "@assets/webp";
import Button from "@components/Button/Button";
export default function FirstPage() {
  const navigate = useNavigate();
  const handleClick = () => {
    navigate(ROUTE_PATHS_MAP.main);
  };
  const { registerElement, getAnimationState } = useAnimationObserver();
  return (
    <>
      <S.Layout>
        <S.Container>
          <div
            ref={registerElement("Title1")}
            css={createAnimation({
              animation: "fadeIn",
              shouldAnimate: getAnimationState("Title1").shouldAnimate,
            })}
          >
            <Text textType="heading" css={S.titleStyle}>
              To your route, 투룻!
            </Text>
          </div>

          <div
            ref={registerElement("Button1")}
            css={createAnimation({
              animation: "fadeIn",
              shouldAnimate: getAnimationState("Button1").shouldAnimate,
              delay: 600,
            })}
          >
            <Button onClick={handleClick} css={S.buttonStyle}>
              투룻 사용하기
            </Button>
          </div>
        </S.Container>
        <S.Gradient />
        <S.Image src={BigTturi} alt="" />
        {/* <Icon
          iconType="down-arrow"
          size="60"
          color={PRIMITIVE_COLORS.white}
          css={S.iconStyle}
        /> */}
      </S.Layout>
    </>
  );
}

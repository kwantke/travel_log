import { Outlet} from "react-router";
import * as S from "./AppLayout.styled";
export default function AppLayout() {
  return (
    <>
      <S.OutletContainer>
        <Outlet />
      </S.OutletContainer>
    </>
  );
}
